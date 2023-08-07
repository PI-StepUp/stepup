import React, {useState, useRef, useEffect, useCallback} from "react";
import io from 'socket.io-client';

import SideMenu from "components/SideMenu";
import Video from "components/Video";

import Image from "next/image";
import LeftArrowIcon from "/public/images/icon-left-arrow.svg"
import GroupIcon from "/public/images/icon-group.svg"
import ReflectIcon from "/public/images/icon-reflect.svg"
import CameraIcon from "/public/images/icon-camera.svg"
import MicIcon from "/public/images/icon-mic.svg"
import MoreIcon from "/public/images/icon-more-dot.svg"
import ChatDefaultImg from "/public/images/chat-default-profile-img.svg"
import sendImg from "/public/images/send-img.svg"
import ReflectHoverIcon from "/public/images/icon-hover-reflect.svg"
import MicHoverIcon from "/public/images/icon-hover-mic.svg"
import CameraHoverIcon from "/public/images/icon-hover-camera.svg"
import MoreDotHoverIcon from "/public/images/icon-hover-more-dot.svg"

import { accessTokenState, refreshTokenState, idState, nicknameState } from "states/states";
import { useRecoilState } from "recoil";
import { LanguageState } from "states/states";
import { createLandmarker, calculateSimilarity } from "../../utils/motionsetter";
import { PoseLandmarker } from "@mediapipe/tasks-vision";
import axios from "axios";
import { useRouter } from "next/router";

const pc_config = {
	iceServers: [
		// {
		//   urls: 'stun:[STUN_IP]:[PORT]',
		//   'credentials': '[YOR CREDENTIALS]',
		//   'username': '[USERNAME]'
		// },
		{
			urls: 'stun:stun.l.google.com:19302',
		},
	],
};
const SOCKET_SERVER_URL = 'http://localhost:4002';

const EMBED_URL = {
    13: "",
    14: "https://www.youtube.com/embed/gV4j4oKnA7s",
    15: "https://www.youtube.com/embed/xCb9V33T-D8",
    16: "https://www.youtube.com/embed/GMSLYWc_UX4",
    17: "https://www.youtube.com/embed/hcaAQCyXurg",
}

const DanceRoom = () => {
    const [lang, setLang] = useRecoilState(LanguageState);
    const socketRef = useRef<SocketIOClient.Socket>();
	const pcsRef = useRef<{ [socketId: string]: RTCPeerConnection }>({});
	const localVideoRef = useRef<HTMLVideoElement>(null);
	const localStreamRef = useRef<MediaStream>();
	const [users, setUsers] = useState<WebRTCUser[]>([]);

    const [msgList, setMsgList] = useState<any[]>([]);
    const [accessToken, setAccessToken] = useRecoilState(accessTokenState);
    const [refreshToken, setRefreshToken] = useRecoilState(refreshTokenState);
    const [count3, setCount3] = useState(false);
    const [count2, setCount2] = useState(false);
    const [count1, setCount1] = useState(false);
    const [id, setId] = useRecoilState(idState);
    const [correct, setCorrect] = useState(0);
    const [nickname, setNickname] = useRecoilState(nicknameState);
    const [playResult, setPlayResult] = useState('');
    const [urlNo, setUrlNo] = useState<any>(0);
    const inputChat = useRef<any>(null);
    const chatContent = useRef<any>(null);
    const [end, setEnd] = useState(false);
    const router = useRouter();
    const roomId = router.query.roomId;

    let danceRecord: any[] = [];

    // 컨트롤러 hover 시 변경
    const [reflect, setReflect] = useState(false);
    const [mic, setMic] = useState(false);
    const [camera, setCamera] = useState(false);
    const [moredot, setMoredot] = useState(false);
    const reflectHover = () => {
        setReflect(true);
    }
    const reflectLeave = () => {
        setReflect(false);
    }
    const micHover = () => {
        setMic(true);
    }
    const micLeave = () => {
        setMic(false);
    }
    const cameraHover = () => {
        setCamera(true);
    }
    const cameraLeave = () => {
        setCamera(false);
    }
    const moreDotHover = () => {
        setMoredot(true);
    }
    const moreDotLeave = () => {
        setMoredot(false);
    }

    const youtubeChange = (e: any) => {
        setEnd(true);
    }

    const sendMessage = () => {
        socketRef.current.emit("send_message", inputChat.current?.value, roomId);
        socketRef.current.on('message', (data:any) => {
            setMsgList([...msgList, data]);
        })
        if(inputChat.current != null){
            inputChat.current.value = "";
        }
        scrollToBottom();
    }

    const scrollToBottom = () => {
        if(chatContent.current != null){
            chatContent.current.scrollTop = chatContent.current.scrollHeight;
        }
    }

    const handleKeyPress = (e: KeyboardEvent<HTMLInputElement>) => {
        if (e.key === 'Enter') {
            socketRef.current.emit("send_message", inputChat.current?.value, roomId);
            socketRef.current.on('message', (data:any) => {
                setMsgList([...msgList, data]);
            })
            if(inputChat.current != null){
                inputChat.current.value = "";
            }
            scrollToBottom();
        }
    };

	const getLocalStream = useCallback(async () => {
		try {
			const localStream = await navigator.mediaDevices.getUserMedia({
				audio: true,
				video: {
					width: 240,
					height: 240,
				},
			});
			localStreamRef.current = localStream;
			if (localVideoRef.current){
                localVideoRef.current.srcObject = localStream;
                localVideoRef.current.play();
                console.log("video 실행");
                localVideoRef.current.addEventListener("loadeddata", predictWebcam);
            }
			if (!socketRef.current) return;
			socketRef.current.emit('join_room', {
				room: roomId,
				email: 'sample@naver.com',
			});
		} catch (e) {
			console.log(`getUserMedia error: ${e}`);
		}
	}, []);

	const createPeerConnection = useCallback((socketID: string, email: string) => {
		try {
			const pc = new RTCPeerConnection(pc_config);

			pc.onicecandidate = (e) => {
				if (!(socketRef.current && e.candidate)) return;
				console.log('onicecandidate');
				socketRef.current.emit('candidate', {
					candidate: e.candidate,
					candidateSendID: socketRef.current.id,
					candidateReceiveID: socketID,
				});
			};

			pc.oniceconnectionstatechange = (e) => {
				console.log(e);
			};

			pc.ontrack = (e) => {
				console.log('ontrack success');
				setUsers((oldUsers) =>
					oldUsers
						.filter((user) => user.id !== socketID)
						.concat({
							id: socketID,
							email,
							stream: e.streams[0],
						}),
				);
			};

			if (localStreamRef.current) {
				console.log('localstream add');
				localStreamRef.current.getTracks().forEach((track) => {
					if (!localStreamRef.current) return;
					pc.addTrack(track, localStreamRef.current);
				});
			} else {
				console.log('no local stream');
			}

			return pc;
		} catch (e) {
			console.error(e);
			return undefined;
		}
	}, []);

    let poseLandmarker: PoseLandmarker;

	useEffect(() => {
		
	}, []);

	let lastVideoTime: number | undefined = -1;
	let frameCount = 0;
	let saveMotion: Boolean = false;

	// ============ 모션 인식 =============
	async function predictWebcam() {
		if (!poseLandmarker) {
			console.log("pose landmarker not loaded!");
			return;
		}

		let startTimeMs = await performance.now();


		if (lastVideoTime !== localVideoRef.current?.currentTime) {
			lastVideoTime = localVideoRef.current?.currentTime;

			await poseLandmarker.detectForVideo(localVideoRef.current, startTimeMs, (result) => {
				// console.log("detective 실행");
				frameCount += 1;

				if (saveMotion) {
					console.log(result);
					setDance(result, danceRecord);
				}

			});
		}

		// Call this function again to keep predicting when the browser is ready.
		if (!localVideoRef.current?.paused) {
			window.requestAnimationFrame(predictWebcam);
		}
	}

	// ========== 안무 좌표 저장 ============

	async function setDance(result: any, dance: any[]) {
		let coordinate: any[] = [];
		let oneFrame = [];

		for (let i = 11; i < 29; i++) {
			if (17 <= i && i <= 22) continue;

			if (typeof result.landmarks[0] != "undefined") {
				coordinate = [result.landmarks[0][i].x, result.landmarks[0][i].y];
			}
			oneFrame.push(coordinate);
		}

		dance.push(oneFrame);
	}

	// =====================================

	// ==============정답 데이터 get=====================

    async function getAnswerData(musicId:number) {
        try{
            await axios.post('http://52.78.93.184:8080/api/user/login', {
                id: "ssafy",
                password: "ssafy",
            }).then((data) => {
                setAccessToken(data.data.data.tokens.accessToken);
            })
        }catch(e){
            console.error(e);
        }
        try {
            const response = await axios.get(`http://52.78.93.184:8080/api/music/${musicId}`, {
                params:{
                    musicId: musicId,
                },
                headers: {
                    Authorization: `Bearer ${accessToken}` 
                },
            });
            const responseData = await response.data;
            console.log("successfully!");
            console.log("responseData 값", responseData);
            return await responseData;
        } catch (error) {
            console.error("Error:", error);
        }
        
    }

	useEffect(() => {
		socketRef.current = io.connect(SOCKET_SERVER_URL);
		getLocalStream();

		socketRef.current.on('all_users', (allUsers: Array<{ id: string; email: string }>) => {
			allUsers.forEach(async (user) => {
				if (!localStreamRef.current) return;
				const pc = createPeerConnection(user.id, user.email);
				if (!(pc && socketRef.current)) return;
				pcsRef.current = { ...pcsRef.current, [user.id]: pc };
				try {
					const localSdp = await pc.createOffer({
						offerToReceiveAudio: true,
						offerToReceiveVideo: true,
					});
					console.log('create offer success');
					await pc.setLocalDescription(new RTCSessionDescription(localSdp));
					socketRef.current.emit('offer', {
						sdp: localSdp,
						offerSendID: socketRef.current.id,
						offerSendEmail: 'offerSendSample@sample.com',
						offerReceiveID: user.id,
					});
				} catch (e) {
					console.error(e);
				}
			});
		});

		socketRef.current.on(
			'getOffer',
			async (data: {
				sdp: RTCSessionDescription;
				offerSendID: string;
				offerSendEmail: string;
			}) => {
				const { sdp, offerSendID, offerSendEmail } = data;
				console.log('get offer');
				if (!localStreamRef.current) return;
				const pc = createPeerConnection(offerSendID, offerSendEmail);
				if (!(pc && socketRef.current)) return;
				pcsRef.current = { ...pcsRef.current, [offerSendID]: pc };
				try {
					await pc.setRemoteDescription(new RTCSessionDescription(sdp));
					console.log('answer set remote description success');
					const localSdp = await pc.createAnswer({
						offerToReceiveVideo: true,
						offerToReceiveAudio: true,
					});
					await pc.setLocalDescription(new RTCSessionDescription(localSdp));
					socketRef.current.emit('answer', {
						sdp: localSdp,
						answerSendID: socketRef.current.id,
						answerReceiveID: offerSendID,
					});
				} catch (e) {
					console.error(e);
				}
			},
		);

		socketRef.current.on(
			'getAnswer',
			(data: { sdp: RTCSessionDescription; answerSendID: string }) => {
				const { sdp, answerSendID } = data;
				console.log('get answer');
				const pc: RTCPeerConnection = pcsRef.current[answerSendID];
				if (!pc) return;
				pc.setRemoteDescription(new RTCSessionDescription(sdp));
			},
		);

		socketRef.current.on(
			'getCandidate',
			async (data: { candidate: RTCIceCandidateInit; candidateSendID: string }) => {
				console.log('get candidate');
				const pc: RTCPeerConnection = pcsRef.current[data.candidateSendID];
				if (!pc) return;
				await pc.addIceCandidate(new RTCIceCandidate(data.candidate));
				console.log('candidate add success');
			},
		);

		socketRef.current.on('user_exit', (data: { id: string }) => {
			if (!pcsRef.current[data.id]) return;
			pcsRef.current[data.id].close();
			delete pcsRef.current[data.id];
			setUsers((oldUsers) => oldUsers.filter((user) => user.id !== data.id));
		});

        socketRef.current.on('cntCorrect', (roomName: any) => {
            if(roomId == roomName){
                socketRef.current.emit('close_randomplay', nickname, correct, roomName);
            }
        });

        socketRef.current.on("congraturation", (roomName: any, winner: any) => {
            if(roomId == roomName){
                alert(`${winner}님 1등을 축하드립니다.`);
            }
        })

        socketRef.current.on("startRandomplay", async (musicId: number) => {
            await setCount3(true);
            await setTimeout(async() => {
                await setCount3(false);
                await setCount2(true);
                setTimeout(async () => {
                    await setCount2(false);
                    await setCount1(true);
                    setTimeout(async () => {
                        await setCount1(false);
                        setUrlNo(musicId);
                        // 안무 유사도 측정
                        danceRecord = [];
                        saveMotion = true;
                        
                        // TODO : 선택된 노래 pk 전달
                        const response = await getAnswerData(musicId);
                        console.log("response 값", response);

                        const danceAnswer = await JSON.parse(response.data.answer);
                        console.log("danceAnswer 값", response.data);
                        
                        setTimeout(async () => {
                            saveMotion = false;

                            await calculateSimilarity(danceRecord, danceAnswer).then((score) => {
                                console.log("score", score);
                                if(score < 60){
                                    setPlayResult("failure");
                                    setTimeout(() => {
                                        setPlayResult("");
                                    }, 5000)
                                }else{
                                    setPlayResult("success");
                                    setTimeout(() => {
                                        setPlayResult("");
                                    }, 5000)
                                }
                            });

                            // TODO : 선택된 노래의 playTime으로 설정
                        }, (response.data.playtime+2)*1000);

                    }, 2000);
                }, 2000);
            }, 2000);
        });

		return () => {
			if (socketRef.current) {
				socketRef.current.disconnect();
			}
			users.forEach((user) => {
				if (!pcsRef.current[user.id]) return;
				pcsRef.current[user.id].close();
				delete pcsRef.current[user.id];
			});
		};
		// eslint-disable-next-line react-hooks/exhaustive-deps
	}, [createPeerConnection, getLocalStream]);

    return(
        <>
            <div className="practiceroom-wrap">
                <SideMenu/>
                <div className="practice-video-wrap">
                    <div className="practice-title">
                        <div className="pre-icon">
                            <Image src={LeftArrowIcon} alt=""/>
                        </div>
                        <div className="room-title">
                            <h3>랜덤플레이 댄스 방 제목</h3>
                            <span>2013년 7월 3일</span>
                        </div>
                    </div>

                    <div className="video-content">
                        <div className="my-video" style={{ position: "relative", top: "0px", left: "0px" }}>
                            <video src="" playsInline ref={localVideoRef}></video>
                        </div>
                        <div className="yours-video">
                            {users.map((user, index) => (
                                <Video key={index} email={user.email} stream={user.stream} />
                            ))}
                        </div>
                        <div className="control-wrap">
                            <ul>
                                <li onMouseEnter = {reflectHover} onMouseLeave = {reflectLeave}>
                                    <button>
                                    {reflect ? <Image src={ReflectHoverIcon} alt=""/> : <Image src={ReflectIcon} alt=""/>}
                                    </button>
                                </li>
                                <li onMouseEnter = {micHover} onMouseLeave = {micLeave}>
                                    <button>
                                    {mic ? <Image src={MicHoverIcon} alt=""/> : <Image src={MicIcon} alt=""/>}
                                    </button>
                                </li>
                                <li><button className="exit-button">{lang==="en" ? "End Practice" : lang==="cn" ? "结束练习" : "연습 종료하기" }</button></li>
                                <li onMouseEnter = {cameraHover} onMouseLeave = {cameraLeave}>
                                    <button>
                                    {camera ? <Image src={CameraHoverIcon} alt=""/> : <Image src={CameraIcon} alt=""/>}
                                    </button>
                                </li>
                                <li >
                                    <button onMouseEnter = {moreDotHover} onMouseLeave = {moreDotLeave}>
                                    {moredot ? <Image src={MoreDotHoverIcon} alt=""/> : <Image src={MoreIcon} alt=""/>}
                                    </button>
                                </li>
                            </ul>
                        </div>
                    </div>
                    
                </div>
                <div className="participant-wrap">
                    <div className="participant-list">
                        <div className="participant-list-title">
                            <h3>{lang==="en" ? "List of participants" : lang==="cn" ? "参与者列表" : "참여자 목록" }</h3>
                            <span><Image src={GroupIcon} alt=""/>{users.length+1}{lang==="en" ? "people" : lang==="cn" ? "个人" : "명" }</span>
                        </div>
                        <div className="participant-list-content">
                            <ul>
                                <li className="on">
                                    <Image src={ChatDefaultImg} alt=""/>
                                    <span>임시 이름</span>
                                </li>
                                {
                                    users.map((data) => {
                                        return(
                                            <li>
                                                <Image src={ChatDefaultImg} alt=""/>
                                                <span>{data.id}</span>
                                            </li>
                                        )
                                    })
                                }
                            </ul>
                        </div>
                    </div>
                    <div className="chat-wrap">
                        <div className="chat-title">
                            <h3>{lang==="en" ? "Chatting" : lang==="cn" ? "聊天" : "채팅하기" }</h3>
                        </div>
                        <div className="chat-content-wrap">
                            <div className="chat-read" ref={chatContent}>
                                <ul className="chat-content">
                                {
                                    msgList.map((data) => {
                                        return (
                                            <>
                                                <li>
                                                    <div className="chat-user-img">
                                                        <Image src={ChatDefaultImg} alt=""/>
                                                    </div>
                                                    <div className="chat-user-msg">
                                                        <span>김싸피</span>
                                                        <p>{data}</p>
                                                    </div>
                                                </li>
                                            </>
                                        )
                                    })
                                }
                                </ul>
                            </div>
                            <div className="chat-write">
                                <input type="text" ref={inputChat} onKeyPress={handleKeyPress} placeholder={lang==="en" ? "Send to everyone" : lang==="cn" ? "传送给所有人" : "모두에게 전송" }/>
                                <button onClick={sendMessage}><Image src={sendImg} alt=""/></button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            {
                count3 ?
                <div className="count-3" style={{animationName: "big-animation"}}>3</div>
                :
                <></>
            }
            {
                count2 ?
                <div className="count-2" style={{animationName: "big-animation"}}>2</div>
                :
                <></>
            }
            {
                count1 ?
                <div className="count-1" style={{animationName: "big-animation"}}>1</div>
                :
                <></>
            }
            {
                playResult === "success" ?
                <>
                    <div className="confetti">
                        <div className="confetti-piece"></div>
                        <div className="confetti-piece"></div>
                        <div className="confetti-piece"></div>
                        <div className="confetti-piece"></div>
                        <div className="confetti-piece"></div>
                        <div className="confetti-piece"></div>
                        <div className="confetti-piece"></div>
                        <div className="confetti-piece"></div>
                        <div className="confetti-piece"></div>
                        <div className="confetti-piece"></div>
                        <div className="confetti-piece"></div>
                        <div className="confetti-piece"></div>
                        <div className="confetti-piece"></div>
                    </div>
                    <div className="result-logo">
                        <svg width="450" height="78" viewBox="0 0 450 78" fill="none" xmlns="http://www.w3.org/2000/svg">
                            <path d="M28.7 77.4C19 77.4 8.6 73.8 0.9 66.7L11.1 54.4C16.3 58.9 23.2 62 29.1 62C35.7 62 38.8 59.5 38.8 55.5C38.8 51.2 34.7 49.8 28.3 47.1L18.8 43.1C11 40 3.7 33.5 3.7 22.7C3.7 10.2 14.9 0.199998 30.7 0.199998C39.3 0.199998 48.4 3.5 54.9 10L46 21.2C41.1 17.5 36.5 15.5 30.7 15.5C25.2 15.5 21.9 17.7 21.9 21.6C21.9 25.8 26.5 27.4 33.1 30L42.4 33.7C51.6 37.4 57.1 43.6 57.1 54.1C57.1 66.5 46.7 77.4 28.7 77.4ZM99.2672 77.4C79.3672 77.4 69.0672 66.1 69.0672 41V1.49999H86.8672V42.9C86.8672 57 91.0672 62 99.2672 62C107.367 62 111.867 57 111.867 42.9V1.49999H129.067V41C129.067 66.1 119.067 77.4 99.2672 77.4ZM177.237 77.4C158.337 77.4 142.337 64.4 142.337 39.2C142.337 14.4 158.937 0.199998 177.837 0.199998C187.537 0.199998 195.337 4.69999 200.437 9.9L191.137 21.3C187.337 17.9 183.437 15.5 178.137 15.5C168.537 15.5 160.637 24.1 160.637 38.6C160.637 53.4 167.437 62 177.937 62C183.937 62 188.637 59 192.137 55.2L201.537 66.4C195.237 73.7 186.937 77.4 177.237 77.4ZM238.858 77.4C219.958 77.4 203.958 64.4 203.958 39.2C203.958 14.4 220.558 0.199998 239.458 0.199998C249.158 0.199998 256.958 4.69999 262.058 9.9L252.758 21.3C248.958 17.9 245.058 15.5 239.758 15.5C230.158 15.5 222.258 24.1 222.258 38.6C222.258 53.4 229.058 62 239.558 62C245.558 62 250.258 59 253.758 55.2L263.158 66.4C256.858 73.7 248.558 77.4 238.858 77.4ZM274.057 76V1.49999H321.257V16.4H291.957V30.2H316.857V45.2H291.957V61H322.357V76H274.057ZM360.145 77.4C350.445 77.4 340.045 73.8 332.345 66.7L342.545 54.4C347.745 58.9 354.645 62 360.545 62C367.145 62 370.245 59.5 370.245 55.5C370.245 51.2 366.145 49.8 359.745 47.1L350.245 43.1C342.445 40 335.145 33.5 335.145 22.7C335.145 10.2 346.345 0.199998 362.145 0.199998C370.745 0.199998 379.845 3.5 386.345 10L377.445 21.2C372.545 17.5 367.945 15.5 362.145 15.5C356.645 15.5 353.345 17.7 353.345 21.6C353.345 25.8 357.945 27.4 364.545 30L373.845 33.7C383.045 37.4 388.545 43.6 388.545 54.1C388.545 66.5 378.145 77.4 360.145 77.4ZM421.18 77.4C411.48 77.4 401.08 73.8 393.38 66.7L403.58 54.4C408.78 58.9 415.68 62 421.58 62C428.18 62 431.28 59.5 431.28 55.5C431.28 51.2 427.18 49.8 420.78 47.1L411.28 43.1C403.48 40 396.18 33.5 396.18 22.7C396.18 10.2 407.38 0.199998 423.18 0.199998C431.78 0.199998 440.88 3.5 447.38 10L438.48 21.2C433.58 17.5 428.98 15.5 423.18 15.5C417.68 15.5 414.38 17.7 414.38 21.6C414.38 25.8 418.98 27.4 425.58 30L434.88 33.7C444.08 37.4 449.58 43.6 449.58 54.1C449.58 66.5 439.18 77.4 421.18 77.4Z" fill="url(#paint0_linear_458_11)"/>
                            <defs>
                            <linearGradient id="paint0_linear_458_11" x1="-3" y1="33" x2="454" y2="33" gradientUnits="userSpaceOnUse">
                            <stop stop-color="#0D51F2"/>
                            <stop offset="1" stop-color="#0DABF2"/>
                            </linearGradient>
                            </defs>
                        </svg>
                    </div>
                </>
                :
                playResult === "failure" ?
                <div className="result-logo">
                    <svg width="407" height="77" viewBox="0 0 407 77" fill="none" xmlns="http://www.w3.org/2000/svg">
                        <path d="M0.6 75V0.499994H48.1V15.4H18.5V31.7H43.9V46.6H18.5V75H0.6ZM46.3758 75L69.4758 0.499994H90.8758L113.976 75H95.1758L85.8758 37.8C83.8758 30.4 81.9758 21.5 79.9758 13.9H79.5758C77.7758 21.7 75.7758 30.4 73.8758 37.8L64.5758 75H46.3758ZM62.0758 57.9V44.1H98.0758V57.9H62.0758ZM121.791 75V0.499994H139.691V75H121.791ZM156.752 75V0.499994H174.652V60H203.652V75H156.752ZM241.24 76.4C221.34 76.4 211.04 65.1 211.04 40V0.499994H228.84V41.9C228.84 56 233.04 61 241.24 61C249.34 61 253.84 56 253.84 41.9V0.499994H271.04V40C271.04 65.1 261.04 76.4 241.24 76.4ZM287.709 75V0.499994H315.009C330.909 0.499994 344.109 5.99999 344.109 24C344.109 41.5 330.909 48.8 315.009 48.8H305.609V75H287.709ZM305.609 34.7H313.609C322.109 34.7 326.709 31 326.709 24C326.709 17 322.109 14.7 313.609 14.7H305.609V34.7ZM327.909 75L311.809 44.2L323.909 32.5L347.909 75H327.909ZM358.51 75V0.499994H405.71V15.4H376.41V29.2H401.31V44.2H376.41V60H406.81V75H358.51Z" fill="url(#paint0_linear_459_10)"/>
                        <defs>
                        <linearGradient id="paint0_linear_459_10" x1="-8" y1="32" x2="427.5" y2="32" gradientUnits="userSpaceOnUse">
                        <stop stop-color="#0D52F2"/>
                        <stop offset="1" stop-color="#0DAAF2"/>
                        </linearGradient>
                        </defs>
                    </svg>
                </div>
                :
                <></>
            }
            {
                urlNo ?
                <iframe width="420" height="345" src={`${EMBED_URL[urlNo]}?autoplay=1`} allow="autoplay" onLoad={youtubeChange}></iframe>
                :
                <></>
            }
        </>
    )
}

export default DanceRoom;