import React, { useState, useEffect, useCallback, useRef } from "react";

import LeftArrowIcon from "/public/images/icon-left-arrow.svg"
import ReflectIcon from "/public/images/icon-reflect.svg"
import PlayThumbnail from "/public/images/room-playlist-thumbnail.jpg"
import PlayIcon from "/public/images/icon-play.svg"
import ReflectHoverIcon from "/public/images/icon-hover-reflect.svg"

import { useRecoilState } from "recoil";
import { LanguageState, nicknameState } from "states/states";

import Image from "next/image"
import Link from "next/link"

import { axiosMusic, axiosUser, axiosRank } from "apis/axios";
import { accessTokenState, refreshTokenState, idState } from "states/states";

import { createLandmarker, calculateSimilarity } from "../utils/motionsetter";
import { PoseLandmarker, DrawingUtils } from "@mediapipe/tasks-vision";
import axios from "axios";
import { useRouter } from "next/router";

const EMBED_URL: any = {
    1: "https://www.youtube.com/embed/g4vaGXR7fUY",
    2: "https://www.youtube.com/embed/VyQ40mNYx3Q",
    3: "https://www.youtube.com/embed/2U7QXp9ItqM",
    4: "https://www.youtube.com/embed/WhPr4rC-bAU",
    5: "https://www.youtube.com/embed/bGa1g4jg-MA",
    6: "https://www.youtube.com/embed/klNlrKzcQbc",
    7: "https://www.youtube.com/embed/quT7eRenhxw",
    8: "https://www.youtube.com/embed/Y063oeFDIGA",
    9: "https://www.youtube.com/embed/w-TfkfN6vrw",
    10: "https://www.youtube.com/embed/V9SmaLFFPqM",
    11: "https://www.youtube.com/embed/PYWxkQzp1oY",
    12: "https://www.youtube.com/embed/3we9E99GK2A",
    13: "https://www.youtube.com/embed/rHwdB-J49Ks",
    14: "https://www.youtube.com/embed/mB0tL-7M6VQ",
    15: "https://www.youtube.com/embed/yGJ-1LkaRho",
    16: "https://www.youtube.com/embed/HZb7CWKUaOw",
    17: "https://www.youtube.com/embed/39Y8PkJRZTw",
    18: "https://www.youtube.com/embed/9pIGXdUgCmE",
    19: "https://www.youtube.com/embed/r4jiLONU8R8",
    20: "https://www.youtube.com/embed/n4I0dwD6u1k",
    21: "https://www.youtube.com/embed/I_GEa-Dud6A",
    22: "https://www.youtube.com/embed/4cxU3TJV-CQ",
    23: "https://www.youtube.com/embed/FUxKgi_BDVI",
    24: "https://www.youtube.com/embed/wWJU-nYV-no",
    25: "https://www.youtube.com/embed/QVyaGJRsaVI",
    26: "https://www.youtube.com/embed/CMCKkVbzGfU",
    27: "https://www.youtube.com/embed/FbY8w-eVuz0",
    28: "https://www.youtube.com/embed/VuEh-4UKfqs",
    29: "https://www.youtube.com/embed/B9mgikpIl98",
    30: "https://www.youtube.com/embed/RcojjcsBkUo",
    31: "https://www.youtube.com/embed/yIX33lK7vpo",
    32: "https://www.youtube.com/embed/TtRBMl-K9Xs",
    33: "https://www.youtube.com/embed/pgCyvRoDpB0",
    34: "https://www.youtube.com/embed/1E5Q1AdAxMg",
    35: "https://www.youtube.com/embed/JteGnlZC8K4",
    36: "https://www.youtube.com/embed/RdjCtKJWNVY",
    37: "https://www.youtube.com/embed/yln8wDZ-i4E",
    38: "https://www.youtube.com/embed/96gMuaVE-Bo",
    39: "https://www.youtube.com/embed/pxNSGBU82GY",
    40: "https://www.youtube.com/embed/XknszxBeP7Y",
}

let poseLandmarker: PoseLandmarker;
let drawingUtils: DrawingUtils;
let canvasCtx: any;
let saveMotion = false;
let danceRecord: any[] = [];

const PracticeRoom = () => {
	const [lang, setLang] = useRecoilState(LanguageState);
	const [reflect, setReflect] = useState(false);
	const [accessToken, setAccessToken] = useRecoilState(accessTokenState);
	const [refreshToken, setRefreshToken] = useRecoilState(refreshTokenState);
	const [id, setId] = useRecoilState(idState);
    const [nickname, setNickname] = useRecoilState(nicknameState);
	const [musics, setMusics] = useState<any>();
	const [count3, setCount3] = useState(false);
	const [count2, setCount2] = useState(false);
	const [count1, setCount1] = useState(false);
	const [playResult, setPlayResult] = useState('');
	const [resultScore, setResultScore] = useState<number>();
	const [selectedMusic, setSelectedMusic] = useState<any>(14);
	const [scoreCount, setScoreCount] = useState<any>(0);
    const [refreshScore, setRefreshScore] = useState<any>(0);
	const localVideoRef = useRef<any>(null);
	const localCanvasRef = useRef<HTMLCanvasElement>(null);
	const localStreamRef = useRef<MediaStream>();
    const myVideoDivRef = useRef<HTMLDivElement>(null);
    const router = useRouter();

	const reflectHover = () => {
		setReflect(true);
	}
	const reflectLeave = () => {
		setReflect(false);
	}

	const getLocalStream = useCallback(async () => {
		try {
			const localStream = await navigator.mediaDevices.getUserMedia({
				audio: false,
				video: {
					width: 240,
					height: 240,
				},
			});
			localStreamRef.current = localStream;
			if (localVideoRef.current) {
				localVideoRef.current.srcObject = localStream;
				localVideoRef.current.play();
				localVideoRef.current.addEventListener("loadeddata", makePoseLandmarker);
			}
		} catch (e) {
			console.log(`getUserMedia error: ${e}`);
		}
	}, []);

    useEffect(() => {
        getLocalStream();

        axios.get("https://stepup-pi.com:8080/api/music",{
            params:{
                keyword: "",
            },
            headers:{
                Authorization: `Bearer ${accessToken}`,
            }
        }).then((data) => {
            if(data.data.message === "노래 목록 조회 완료"){
                setMusics(data.data.data);
            }
        }).catch((error: any) => {
            if(error.response.data.message === "만료된 토큰"){
                axiosMusic.get("",{
                    params:{
                        keyword: "",
                    },
                    headers:{
                        refreshToken: refreshToken,
                    }
                }).then((data) => {
                    if(data.data.message === "토큰 재발급 완료"){
                        setAccessToken(data.data.data.accessToken);
                        setRefreshToken(data.data.data.refreshToken);
                    }
                }).then(() => {
                    axios.get("https://stepup-pi.com:8080/api/music",{
                        params:{
                            keyword: "",
                        },
                        headers:{
                            Authorization: `Bearer ${accessToken}`,
                        }
                    }).then((data) => {
                        if(data.data.message === "노래 목록 조회 완료"){
                            setMusics(data.data.data);
                        }
                    })
                }).catch((data) => {
                    if(data.response.status === 401){
                        alert("장시간 이용하지 않아 자동 로그아웃 되었습니다.");
                        setNickname("");
                        router.push("/login");
                        return;
                    }

                    if(data.response.status === 500){
                        alert("시스템 에러, 관리자에게 문의하세요.");
                        return;
                    }
                })
            }
        })
    }, [])

    const changeMusic = (musicId: any) => {
        setSelectedMusic(musicId);
    }

    // ======= PoseLandmarker 생성 =======
	async function makePoseLandmarker() {
		canvasCtx = localCanvasRef.current!.getContext("2d");
		drawingUtils = new DrawingUtils(canvasCtx);
		await createLandmarker().then((landMarker) => {
			poseLandmarker = landMarker;
		});
	}

	async function startMeasure() {
		await setCount3(true);
		await setTimeout(async () => {
			await setCount3(false);
			await setCount2(true);
			setTimeout(async () => {
				await setCount2(false);
				await setCount1(true);
				setTimeout(async () => {
					await setCount1(false);

					await startPredictAndCalcSimilarity(selectedMusic);
				}, 2000);
			}, 2000);
		}, 2000);
	}


	let lastVideoTime: number | undefined = -1;

	async function startPredictAndCalcSimilarity(musicId: number) {
		saveMotion = true;
		localCanvasRef.current!.style.display = "block";
		danceRecord = [];

		const response = await getAnswerData(musicId);
		console.log("response 값", response);

		predictWebcam();

		return setTimeout(async () => {
			saveMotion = false;
			localCanvasRef.current!.style.display = "none";

			const danceAnswer = await JSON.parse(response.data.answer);

			const score = await calculateSimilarity(danceRecord, danceAnswer);
			setResultScore(score);
            await setRefreshScore(score);

			setTimeout(() => {
				setPlayResult("");
			}, 5000)

	        if (score < 50) {
                setPlayResult("fail");
                setTimeout(() => {
                    setPlayResult("");
                }, 5000)
            } else if (score < 60) {
                setPlayResult("bad");
            }
            else if (score < 70) {
                setPlayResult("soso");
            } else if (score < 80) {
                setPlayResult("good");
            } else if (score < 90) {
                setPlayResult("great");
            }
            else setPlayResult("perfect");

			axiosRank.post(`/point`, {
				id: id,
				pointPolicyId: 6,
				randomDanceId: null,
				count: 1,
			}, {
				headers: {
					Authorization: `Bearer ${accessToken}`
				}
			}).then((data) => {
				if (data.data.message === "포인트 적립 완료") {
					// alert("연습실 이용 포인트가 적립되었습니다!");
				}
			}).catch((error: any) => {
                if(error.response.data.message === "만료된 토큰"){
                    axiosRank.post(`/point`, {
                        id: id,
                        pointPolicyId: 6,
                        randomDanceId: null,
                        count: 1,
                    }, {
                        headers: {
                            refreshToken: refreshToken,
                        }
                    }).then((data) => {
                        if(data.data.message === "토큰 재발급 완료"){
                            setAccessToken(data.data.data.accessToken);
                            setRefreshToken(data.data.data.refreshToken);
                        }
                    }).then(() => {
                        axiosRank.post(`/point`, {
                            id: id,
                            pointPolicyId: 6,
                            randomDanceId: null,
                            count: 1,
                        }, {
                            headers: {
                                Authorization: `Bearer ${accessToken}`
                            }
                        }).then((data) => {
                            if (data.data.message === "포인트 적립 완료") {
                                // alert("연습실 이용 포인트가 적립되었습니다!");
                            }
                        })
                    }).catch((data) => {
                        if(data.response.status === 401){
                            alert("장시간 이용하지 않아 자동 로그아웃 되었습니다.");
                            setNickname("");
                            router.push("/login");
                            return;
                        }
    
                        if(data.response.status === 500){
                            alert("시스템 에러, 관리자에게 문의하세요.");
                            return;
                        }
                    }).catch((e) => {
                        console.log("포인트 지급 에러 발생", e);
                        alert("연습실 이용 포인트 적립에 오류가 발생했습니다. 관리자에게 문의 바랍니다.");
                    })
                }
            })
			return score;
		}, (response.data.playtime + 2) * 1000);
	}

	let frameCount = 0;
	// ============ 모션 인식 =============
	async function predictWebcam() {
		if (!poseLandmarker) {
			await makePoseLandmarker();
			predictWebcam();
			return;
		}

		let startTimeMs = await performance.now();
		frameCount = 0;


		if (lastVideoTime !== localVideoRef.current?.currentTime) {
			lastVideoTime = localVideoRef.current?.currentTime;

			await poseLandmarker.detectForVideo(localVideoRef.current, startTimeMs, (result) => {
				frameCount += 1;
				setDance(result, danceRecord);

				canvasCtx.save();
				canvasCtx.clearRect(0, 0, localCanvasRef.current!.width, localCanvasRef.current!.height);
				for (const landmark of result.landmarks) {
					drawingUtils.drawLandmarks(landmark, {
						radius: (data: any) => DrawingUtils.lerp(data.from!.z, -0.15, 0.1, 5, 1)
					});
					drawingUtils.drawConnectors(landmark, PoseLandmarker.POSE_CONNECTIONS);
				}
				canvasCtx.restore();
			});
		}

		if (saveMotion) {
			window.requestAnimationFrame(predictWebcam);
		}
	}

	// ========== 안무 좌표 저장 ============

	async function setDance(result: any, dance: any[]) {
		let coordinate;
		let oneFrame = [];

		for (let i = 11; i < 29; i++) {
			if (17 <= i && i <= 22) continue;

			if (typeof result.landmarks[0] !== "undefined") {
				coordinate = [result.landmarks[0][i].x, result.landmarks[0][i].y, result.landmarks[0][i].z];
			}
			oneFrame.push(coordinate);
		}

		dance.push(oneFrame);
	}
    
	// =====================================

	// ==============정답 데이터 get=====================

    async function getAnswerData(musicId:number) {
        try{
            await axiosUser.post('/login', {
                id: "ssafy",
                password: "ssafy",
            }).then((data) => {
                setAccessToken(data.data.data.tokens.accessToken);
                setRefreshToken(data.data.data.tokens.refreshToken);
            })
        }catch(e){
            console.error(e);
        }

        try {
            const response = await axiosMusic.get(`/${musicId}`, {
                params:{
                    musicId: musicId,
                },
                headers: {
                    Authorization: `Bearer ${accessToken}`, 
                },
            })
            const responseData = await response.data;
            return await responseData;
        } catch (error) {
            console.error("Error:", error);
        }
        
    }

    function easeOutCirc(x: number): number {
        return Math.sqrt(1 - Math.pow(x - 1, 2));
    }

    function useCountNum(end: number, start = 0, duration = 2000) {
        const frameRate = 1000 / 60
        const totalFrame = Math.round(duration / frameRate)
        setScoreCount(0);
      
        useEffect(() => {
          let currentNumber = start
          const counter = setInterval(() => {
            const progress = easeOutCirc(++currentNumber / totalFrame)
            setScoreCount(Math.round(end * progress))
      
            if (progress === 1) {
              clearInterval(counter)
            }
          }, frameRate)
        }, [end, frameRate, start, totalFrame])
      
        return scoreCount;
    }

    async function reflectMyVideo() {
        if (!reflect) {
            myVideoDivRef.current?.setAttribute("class", "my-video reflect-video");
            setReflect(true);
        } else {
            myVideoDivRef.current?.setAttribute("class", "my-video");
            setReflect(false);
        }
    }

    const stopMediaTracks = (stream: any) => {
        if (!stream) return;
        stream.getTracks().forEach((track: any) => {
          track.stop();
        });
    };

    useEffect(() => {
        return () => {
            if (localStreamRef.current) {
                const videoTrack = localStreamRef.current.getVideoTracks()[0];
                if (videoTrack) {
                    videoTrack.stop();
                }
            }

            stopMediaTracks(localStreamRef.current);

            localVideoRef.current = null;
        }
    }, [])

    return(
        <>
            <div className="practiceroom-wrap">
                {/* <SideMenu/> */}
                <div className="remove-sidemenu practice-video-wrap">
                    <div className="practice-title">
                        <div className="pre-icon">
                            <Link href="/"><Image src={LeftArrowIcon} alt=""/></Link>
                        </div>
                        <div className="room-title">
                            <h3>STEP UP 연습실</h3>
                            <span>KPOP 커버에 도전해 더 높은 점수를 노려보세요!</span>
                        </div>
                        <div className="score-wrap">
                            <span>안무 정확도: {refreshScore.toString() === "NaN" ? "측정중" : refreshScore.toString() + "점"}</span>
                        </div>
                    </div>

                    <div className="video-content">
                        <div className="my-video youtube-video">
                        {
                            saveMotion ?
                            <iframe className="make-border" src={`${EMBED_URL[selectedMusic]}?autoplay=1`} allow="autoplay"></iframe>
                            :
                            <iframe className="make-border" src={`${EMBED_URL[selectedMusic]}`} allow="autoplay"></iframe>
                        }                            
                        </div>

                        <div ref={myVideoDivRef} className="my-video reflect-video" style={{ position: "relative", top: "0px", left: "0px" }}>
                            <video className="make-border" src="" playsInline ref={localVideoRef}></video>
                            <canvas ref={localCanvasRef} style={{position:"absolute", left: "0px", top: "0px", width: "100%", height:"100%"}}></canvas>
                        </div>
                        
                        <div className="control-wrap">
                            <ul>
                                <li onMouseEnter = {reflectHover} onMouseLeave = {reflectLeave}>
                                    <button onClick={reflectMyVideo}>
                                        {reflect ? <Image src={ReflectHoverIcon} alt=""/> : <Image src={ReflectIcon} alt=""/>}
                                    </button>
                                </li>
                                <li><button onClick={startMeasure} className="exit-button">{lang==="en" ? "Perfect Score" : lang==="cn" ? "满分" : "퍼펙트 스코어" }</button></li>
                            </ul>
                        </div>
                    </div>
                    
                </div>
                <div className="musiclist">
                    <div className="musiclist-title">
                        <h3>{lang==="en" ? "List of practice rooms" : lang==="cn" ? "练习室列表" : "연습실 목록" }</h3>
                        <span>{musics?.length}</span>
                    </div>
                    <div className="musiclist-content">
                        <ul>
                            {musics?.map((music:any, index: any) => {
                                return(
                                    <li key={index} onClick={() => changeMusic(music.musicId)}>
                                        <div className="flex-wrap">
                                            <div className="musiclist-content-thumbnail">
                                                {
                                                    music.url ?
                                                    <Image src={music.url} alt="" width={40} height={40}/>
                                                    :
                                                    <Image src={PlayThumbnail} alt=""/>
                                                }
                                                
                                            </div>
                                            <div className="musiclist-content-info">
                                                <h4>{music.title}</h4>
                                                <span>{music.artist}</span>
                                            </div>
                                        </div>
                                        <div className="musiclist-content-control-icon">
                                            <span><Image src={PlayIcon} alt=""/></span>
                                        </div>
                                    </li>
                                )
                            })}
                        </ul>
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
                playResult === "perfect" ?
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
                            <svg width="372" height="71" viewBox="0 0 372 71" fill="none" xmlns="http://www.w3.org/2000/svg">
                                <path d="M55 23.9062C55 26.9375 54.5469 29.625 53.6406 31.9688C52.7344 34.3125 51.4844 36.3594 49.8906 38.1094C48.3281 39.8594 46.4844 41.3438 44.3594 42.5625C42.2344 43.7812 39.9531 44.7812 37.5156 45.5625C35.1094 46.3125 32.6094 46.875 30.0156 47.25C27.4219 47.5938 24.875 47.7812 22.375 47.8125V69.6562H0.53125C0.53125 62.5 0.546875 55.375 0.578125 48.2812C0.609375 41.1875 0.65625 34.0312 0.71875 26.8125C0.78125 23 0.796875 19.1875 0.765625 15.375C0.734375 11.5625 0.8125 7.71875 1 3.84375C5 2.65625 9 1.76563 13 1.17188C17 0.578125 21.125 0.28125 25.375 0.28125C27.7812 0.28125 30.1875 0.484375 32.5938 0.890625C35 1.26562 37.2969 1.875 39.4844 2.71875C41.7031 3.5625 43.75 4.625 45.625 5.90625C47.5312 7.15625 49.1719 8.65625 50.5469 10.4062C51.9219 12.1562 53 14.1562 53.7812 16.4062C54.5938 18.625 55 21.125 55 23.9062ZM34.1875 25.5C34.1875 23.4688 33.5625 21.8906 32.3125 20.7656C31.0938 19.6094 29.5 19.0312 27.5312 19.0312C26.875 19.0312 26.1875 19.0937 25.4688 19.2188C24.7812 19.3125 24.125 19.4375 23.5 19.5938L22.9375 33.2812C23.375 33.3438 23.7969 33.375 24.2031 33.375C24.6094 33.375 25.0312 33.375 25.4688 33.375C26.5938 33.375 27.6719 33.1875 28.7031 32.8125C29.7656 32.4375 30.7031 31.9062 31.5156 31.2188C32.3281 30.5 32.9688 29.6719 33.4375 28.7344C33.9375 27.7656 34.1875 26.6875 34.1875 25.5ZM109.891 31.125C109.891 33.8438 109.703 36.5625 109.328 39.2812C103.984 39.625 98.6719 40.1875 93.3906 40.9688C88.1094 41.7188 82.8281 42.4688 77.5469 43.2188C78.3594 45.4688 79.625 47.2656 81.3438 48.6094C83.0938 49.9531 85.1719 50.625 87.5781 50.625C88.8281 50.625 90.1875 50.3906 91.6562 49.9219C93.1562 49.4531 94.6406 48.875 96.1094 48.1875C97.5781 47.4688 98.9844 46.7031 100.328 45.8906C101.672 45.0781 102.828 44.3438 103.797 43.6875L101.547 64.9688C100.328 65.9062 99 66.7031 97.5625 67.3594C96.1562 68.0156 94.6875 68.5469 93.1562 68.9531C91.625 69.3594 90.0781 69.6562 88.5156 69.8438C86.9531 70.0312 85.4219 70.125 83.9219 70.125C80.8281 70.125 78 69.7031 75.4375 68.8594C72.875 68.0156 70.5469 66.8438 68.4531 65.3438C66.3906 63.8125 64.5781 62.0156 63.0156 59.9531C61.4531 57.8906 60.1562 55.6406 59.125 53.2031C58.0938 50.7656 57.3125 48.1875 56.7812 45.4688C56.25 42.75 55.9844 40 55.9844 37.2188C55.9844 34.3125 56.25 31.4062 56.7812 28.5C57.3125 25.5938 58.1094 22.7969 59.1719 20.1094C60.2656 17.4219 61.625 14.9375 63.25 12.6562C64.9062 10.3438 66.8281 8.32813 69.0156 6.60938C71.2031 4.89062 73.6719 3.54688 76.4219 2.57812C79.2031 1.60937 82.2656 1.125 85.6094 1.125C88.3906 1.125 90.9219 1.51562 93.2031 2.29688C95.5156 3.04688 97.5625 4.10938 99.3438 5.48438C101.156 6.82813 102.719 8.4375 104.031 10.3125C105.375 12.1875 106.469 14.25 107.312 16.5C108.188 18.7187 108.828 21.0781 109.234 23.5781C109.672 26.0469 109.891 28.5625 109.891 31.125ZM91.1406 28.6875C91.1406 27.8125 91.0469 26.9219 90.8594 26.0156C90.6719 25.0781 90.3594 24.2344 89.9219 23.4844C89.5156 22.7344 88.9688 22.125 88.2812 21.6562C87.5938 21.1562 86.7344 20.9062 85.7031 20.9062C84.4531 20.9062 83.3438 21.25 82.375 21.9375C81.4375 22.5938 80.625 23.4375 79.9375 24.4688C79.25 25.4688 78.6875 26.5625 78.25 27.75C77.8125 28.9375 77.4844 30.0312 77.2656 31.0312L91.1406 29.7188V28.6875ZM167.922 23.8125C167.922 26.25 167.703 28.4688 167.266 30.4688C166.859 32.4375 166.203 34.25 165.297 35.9062C164.391 37.5625 163.219 39.0938 161.781 40.5C160.344 41.9062 158.609 43.25 156.578 44.5312L167.734 64.4062L146.641 68.5312L139.422 48.5625L133.609 48.75L132.766 68.25H112.609C112.766 61.125 112.906 54.0312 113.031 46.9688C113.188 39.9062 113.359 32.8125 113.547 25.6875C113.609 22.0938 113.672 18.5312 113.734 15C113.797 11.4688 113.922 7.90625 114.109 4.3125C116.297 3.5 118.453 2.82812 120.578 2.29688C122.703 1.76563 124.828 1.35938 126.953 1.07812C129.109 0.765625 131.281 0.5625 133.469 0.46875C135.688 0.34375 137.953 0.28125 140.266 0.28125C143.828 0.28125 147.266 0.78125 150.578 1.78125C153.922 2.75 156.875 4.21875 159.438 6.1875C162 8.15625 164.047 10.6094 165.578 13.5469C167.141 16.4844 167.922 19.9063 167.922 23.8125ZM146.922 25.3125C146.922 24 146.734 22.7969 146.359 21.7031C146.016 20.6094 145.484 19.6719 144.766 18.8906C144.078 18.0781 143.203 17.4531 142.141 17.0156C141.109 16.5469 139.891 16.3125 138.484 16.3125C137.859 16.3125 137.25 16.3594 136.656 16.4531C136.062 16.5156 135.484 16.625 134.922 16.7812L134.172 34.2188H135.297C136.641 34.2188 138 34.0469 139.375 33.7031C140.781 33.3594 142.031 32.8281 143.125 32.1094C144.25 31.3906 145.156 30.4688 145.844 29.3438C146.562 28.2188 146.922 26.875 146.922 25.3125ZM215.547 2.34375C215.359 5.28125 215.188 8.1875 215.031 11.0625C214.875 13.9375 214.672 16.8437 214.422 19.7812L194.547 20.8125V26.7188H211.797L210.859 41.25L194.453 41.7188L194.547 67.5938L170.828 68.0625L172.328 2.34375H215.547ZM269.453 31.125C269.453 33.8438 269.266 36.5625 268.891 39.2812C263.547 39.625 258.234 40.1875 252.953 40.9688C247.672 41.7188 242.391 42.4688 237.109 43.2188C237.922 45.4688 239.188 47.2656 240.906 48.6094C242.656 49.9531 244.734 50.625 247.141 50.625C248.391 50.625 249.75 50.3906 251.219 49.9219C252.719 49.4531 254.203 48.875 255.672 48.1875C257.141 47.4688 258.547 46.7031 259.891 45.8906C261.234 45.0781 262.391 44.3438 263.359 43.6875L261.109 64.9688C259.891 65.9062 258.562 66.7031 257.125 67.3594C255.719 68.0156 254.25 68.5469 252.719 68.9531C251.188 69.3594 249.641 69.6562 248.078 69.8438C246.516 70.0312 244.984 70.125 243.484 70.125C240.391 70.125 237.562 69.7031 235 68.8594C232.438 68.0156 230.109 66.8438 228.016 65.3438C225.953 63.8125 224.141 62.0156 222.578 59.9531C221.016 57.8906 219.719 55.6406 218.688 53.2031C217.656 50.7656 216.875 48.1875 216.344 45.4688C215.812 42.75 215.547 40 215.547 37.2188C215.547 34.3125 215.812 31.4062 216.344 28.5C216.875 25.5938 217.672 22.7969 218.734 20.1094C219.828 17.4219 221.188 14.9375 222.812 12.6562C224.469 10.3438 226.391 8.32813 228.578 6.60938C230.766 4.89062 233.234 3.54688 235.984 2.57812C238.766 1.60937 241.828 1.125 245.172 1.125C247.953 1.125 250.484 1.51562 252.766 2.29688C255.078 3.04688 257.125 4.10938 258.906 5.48438C260.719 6.82813 262.281 8.4375 263.594 10.3125C264.938 12.1875 266.031 14.25 266.875 16.5C267.75 18.7187 268.391 21.0781 268.797 23.5781C269.234 26.0469 269.453 28.5625 269.453 31.125ZM250.703 28.6875C250.703 27.8125 250.609 26.9219 250.422 26.0156C250.234 25.0781 249.922 24.2344 249.484 23.4844C249.078 22.7344 248.531 22.125 247.844 21.6562C247.156 21.1562 246.297 20.9062 245.266 20.9062C244.016 20.9062 242.906 21.25 241.938 21.9375C241 22.5938 240.188 23.4375 239.5 24.4688C238.812 25.4688 238.25 26.5625 237.812 27.75C237.375 28.9375 237.047 30.0312 236.828 31.0312L250.703 29.7188V28.6875ZM317.266 4.96875L315.578 24.75C314.672 24.5 313.781 24.3281 312.906 24.2344C312.031 24.1406 311.141 24.0938 310.234 24.0938C308.016 24.0938 305.891 24.375 303.859 24.9375C301.859 25.5 300.078 26.3594 298.516 27.5156C296.984 28.6719 295.75 30.1406 294.812 31.9219C293.906 33.6719 293.453 35.75 293.453 38.1562C293.453 39.9062 293.703 41.4375 294.203 42.75C294.734 44.0312 295.469 45.1094 296.406 45.9844C297.375 46.8281 298.531 47.4688 299.875 47.9062C301.25 48.3438 302.766 48.5625 304.422 48.5625C305.578 48.5625 306.766 48.4531 307.984 48.2344C309.203 47.9844 310.422 47.6562 311.641 47.25C312.859 46.8438 314.031 46.375 315.156 45.8438C316.281 45.3125 317.328 44.75 318.297 44.1562L317.547 66.1875C316.328 66.8125 315.016 67.375 313.609 67.875C312.203 68.3438 310.75 68.75 309.25 69.0938C307.781 69.4688 306.297 69.75 304.797 69.9375C303.328 70.125 301.922 70.2188 300.578 70.2188C296.141 70.2188 292.094 69.3906 288.438 67.7344C284.812 66.0781 281.688 63.8281 279.062 60.9844C276.469 58.1094 274.453 54.7812 273.016 51C271.578 47.2188 270.859 43.1875 270.859 38.9062C270.859 33.7812 271.578 29 273.016 24.5625C274.484 20.125 276.625 16.2812 279.438 13.0312C282.281 9.75 285.781 7.1875 289.938 5.34375C294.125 3.46875 298.922 2.53125 304.328 2.53125C306.484 2.53125 308.672 2.70313 310.891 3.04688C313.109 3.39062 315.234 4.03125 317.266 4.96875ZM371.406 2.90625L370.938 22.7812L356.969 23.3438L355.281 66.8438L335.5 67.875L333.719 24.375L319.75 25.125L320.312 3L371.406 2.90625Z" fill="url(#paint0_linear_203_24)" />
                                <defs>
                                    <linearGradient id="paint0_linear_203_24" x1="196" y1="1" x2="196" y2="90" gradientUnits="userSpaceOnUse">
                                        <stop stop-color="#0D52F2" />
                                        <stop offset="0.0001" stop-color="#0D52F2" stop-opacity="0.980033" />
                                        <stop offset="0.9999" stop-color="#0D52F2" stop-opacity="0.0416667" />
                                        <stop offset="1" stop-color="#0D52F2" stop-opacity="0" />
                                        <stop offset="1" stop-color="#0D52F2" stop-opacity="0" />
                                    </linearGradient>
                                </defs>
                            </svg>
                        </div>
                    </>
                    :
                    playResult === "great" ?
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
                            <svg width="278" height="71" viewBox="0 0 278 71" fill="none" xmlns="http://www.w3.org/2000/svg">
                                <path d="M59.0625 58.5C57.5938 60.3438 55.7812 61.9531 53.625 63.3281C51.4688 64.7031 49.1562 65.8438 46.6875 66.75C44.2188 67.6562 41.6875 68.3438 39.0938 68.8125C36.5 69.25 34.0312 69.4688 31.6875 69.4688C27.1562 69.4688 22.9844 68.7031 19.1719 67.1719C15.3594 65.6406 12.0625 63.5156 9.28125 60.7969C6.53125 58.0469 4.375 54.7812 2.8125 51C1.25 47.2188 0.46875 43.0938 0.46875 38.625C0.46875 35.2188 0.859375 31.9062 1.64062 28.6875C2.42188 25.4688 3.53125 22.4531 4.96875 19.6406C6.4375 16.8281 8.21875 14.25 10.3125 11.9062C12.4062 9.5625 14.7812 7.54688 17.4375 5.85938C20.0938 4.17188 23 2.875 26.1562 1.96875C29.3438 1.03125 32.7188 0.5625 36.2812 0.5625C37.625 0.5625 39.0625 0.625 40.5938 0.75C42.125 0.84375 43.6406 1.03125 45.1406 1.3125C46.6719 1.5625 48.1562 1.92188 49.5938 2.39062C51.0312 2.85938 52.3438 3.4375 53.5312 4.125L52.2188 22.0312C50.4688 21.375 48.6562 20.9375 46.7812 20.7188C44.9375 20.4688 43.125 20.3438 41.3438 20.3438C38.8438 20.3438 36.5 20.6875 34.3125 21.375C32.1562 22.0312 30.2656 23.0156 28.6406 24.3281C27.0469 25.6406 25.7812 27.2812 24.8438 29.25C23.9062 31.2188 23.4375 33.5 23.4375 36.0938C23.4375 37.7188 23.6562 39.2969 24.0938 40.8281C24.5312 42.3281 25.1875 43.6719 26.0625 44.8594C26.9688 46.0156 28.0781 46.9531 29.3906 47.6719C30.7344 48.3906 32.2812 48.75 34.0312 48.75C35.2188 48.75 36.4375 48.5938 37.6875 48.2812C38.9688 47.9688 40.0625 47.4375 40.9688 46.6875L41.1562 43.0312L29.9062 42.8438L30.4688 28.7812C35.125 28.625 39.7812 28.5 44.4375 28.4062C49.0938 28.2812 53.7812 28.125 58.5 27.9375L59.0625 58.5ZM117.609 23.8125C117.609 26.25 117.391 28.4688 116.953 30.4688C116.547 32.4375 115.891 34.25 114.984 35.9062C114.078 37.5625 112.906 39.0938 111.469 40.5C110.031 41.9062 108.297 43.25 106.266 44.5312L117.422 64.4062L96.3281 68.5312L89.1094 48.5625L83.2969 48.75L82.4531 68.25H62.2969C62.4531 61.125 62.5938 54.0312 62.7188 46.9688C62.875 39.9062 63.0469 32.8125 63.2344 25.6875C63.2969 22.0938 63.3594 18.5312 63.4219 15C63.4844 11.4688 63.6094 7.90625 63.7969 4.3125C65.9844 3.5 68.1406 2.82812 70.2656 2.29688C72.3906 1.76563 74.5156 1.35938 76.6406 1.07812C78.7969 0.765625 80.9688 0.5625 83.1562 0.46875C85.375 0.34375 87.6406 0.28125 89.9531 0.28125C93.5156 0.28125 96.9531 0.78125 100.266 1.78125C103.609 2.75 106.562 4.21875 109.125 6.1875C111.688 8.15625 113.734 10.6094 115.266 13.5469C116.828 16.4844 117.609 19.9063 117.609 23.8125ZM96.6094 25.3125C96.6094 24 96.4219 22.7969 96.0469 21.7031C95.7031 20.6094 95.1719 19.6719 94.4531 18.8906C93.7656 18.0781 92.8906 17.4531 91.8281 17.0156C90.7969 16.5469 89.5781 16.3125 88.1719 16.3125C87.5469 16.3125 86.9375 16.3594 86.3438 16.4531C85.75 16.5156 85.1719 16.625 84.6094 16.7812L83.8594 34.2188H84.9844C86.3281 34.2188 87.6875 34.0469 89.0625 33.7031C90.4688 33.3594 91.7188 32.8281 92.8125 32.1094C93.9375 31.3906 94.8438 30.4688 95.5312 29.3438C96.25 28.2188 96.6094 26.875 96.6094 25.3125ZM172.734 31.125C172.734 33.8438 172.547 36.5625 172.172 39.2812C166.828 39.625 161.516 40.1875 156.234 40.9688C150.953 41.7188 145.672 42.4688 140.391 43.2188C141.203 45.4688 142.469 47.2656 144.188 48.6094C145.938 49.9531 148.016 50.625 150.422 50.625C151.672 50.625 153.031 50.3906 154.5 49.9219C156 49.4531 157.484 48.875 158.953 48.1875C160.422 47.4688 161.828 46.7031 163.172 45.8906C164.516 45.0781 165.672 44.3438 166.641 43.6875L164.391 64.9688C163.172 65.9062 161.844 66.7031 160.406 67.3594C159 68.0156 157.531 68.5469 156 68.9531C154.469 69.3594 152.922 69.6562 151.359 69.8438C149.797 70.0312 148.266 70.125 146.766 70.125C143.672 70.125 140.844 69.7031 138.281 68.8594C135.719 68.0156 133.391 66.8438 131.297 65.3438C129.234 63.8125 127.422 62.0156 125.859 59.9531C124.297 57.8906 123 55.6406 121.969 53.2031C120.938 50.7656 120.156 48.1875 119.625 45.4688C119.094 42.75 118.828 40 118.828 37.2188C118.828 34.3125 119.094 31.4062 119.625 28.5C120.156 25.5938 120.953 22.7969 122.016 20.1094C123.109 17.4219 124.469 14.9375 126.094 12.6562C127.75 10.3438 129.672 8.32813 131.859 6.60938C134.047 4.89062 136.516 3.54688 139.266 2.57812C142.047 1.60937 145.109 1.125 148.453 1.125C151.234 1.125 153.766 1.51562 156.047 2.29688C158.359 3.04688 160.406 4.10938 162.188 5.48438C164 6.82813 165.562 8.4375 166.875 10.3125C168.219 12.1875 169.312 14.25 170.156 16.5C171.031 18.7187 171.672 21.0781 172.078 23.5781C172.516 26.0469 172.734 28.5625 172.734 31.125ZM153.984 28.6875C153.984 27.8125 153.891 26.9219 153.703 26.0156C153.516 25.0781 153.203 24.2344 152.766 23.4844C152.359 22.7344 151.812 22.125 151.125 21.6562C150.438 21.1562 149.578 20.9062 148.547 20.9062C147.297 20.9062 146.188 21.25 145.219 21.9375C144.281 22.5938 143.469 23.4375 142.781 24.4688C142.094 25.4688 141.531 26.5625 141.094 27.75C140.656 28.9375 140.328 30.0312 140.109 31.0312L153.984 29.7188V28.6875ZM232.312 64.9688L209.062 67.9688L206.25 57.375H195.562L193.219 67.9688L169.312 65.625L187.969 3.1875L214.031 1.875L232.312 64.9688ZM204.562 43.0312L201 26.7188L197.531 43.0312H204.562ZM277.125 2.90625L276.656 22.7812L262.688 23.3438L261 66.8438L241.219 67.875L239.438 24.375L225.469 25.125L226.031 3L277.125 2.90625Z" fill="url(#paint0_linear_203_21)" />
                                <defs>
                                    <linearGradient id="paint0_linear_203_21" x1="148.5" y1="1" x2="148.5" y2="108" gradientUnits="userSpaceOnUse">
                                        <stop stop-color="#0D52F2" />
                                        <stop offset="0.0001" stop-color="#0D52F2" stop-opacity="0.980033" />
                                        <stop offset="0.9999" stop-color="#0D52F2" stop-opacity="0.0416667" />
                                        <stop offset="1" stop-color="#0D52F2" stop-opacity="0" />
                                        <stop offset="1" stop-color="#0D52F2" stop-opacity="0" />
                                    </linearGradient>
                                </defs>
                            </svg>
                        </div>
                        </>
                        :
                        playResult === "good" ?
                            <div className="result-logo">
                                <svg width="249" height="73" viewBox="0 0 249 73" fill="none" xmlns="http://www.w3.org/2000/svg">
                                    <path d="M61.5234 61.0625C59.9935 62.9831 58.1055 64.6595 55.8594 66.0918C53.6133 67.5241 51.2044 68.7122 48.6328 69.6562C46.0612 70.6003 43.4245 71.3164 40.7227 71.8047C38.0208 72.2604 35.4492 72.4883 33.0078 72.4883C28.2878 72.4883 23.9421 71.6908 19.9707 70.0957C15.9993 68.5007 12.5651 66.2871 9.66797 63.4551C6.80339 60.5905 4.55729 57.1888 2.92969 53.25C1.30208 49.3112 0.488281 45.0143 0.488281 40.3594C0.488281 36.8112 0.895182 33.3607 1.70898 30.0078C2.52279 26.6549 3.67839 23.5137 5.17578 20.584C6.70573 17.6543 8.5612 14.9688 10.7422 12.5273C12.9232 10.0859 15.3971 7.98633 18.1641 6.22852C20.931 4.4707 23.9583 3.11979 27.2461 2.17578C30.5664 1.19922 34.082 0.710938 37.793 0.710938C39.1927 0.710938 40.6901 0.776042 42.2852 0.90625C43.8802 1.00391 45.459 1.19922 47.0215 1.49219C48.6165 1.7526 50.1628 2.12695 51.6602 2.61523C53.1576 3.10352 54.5247 3.70573 55.7617 4.42188L54.3945 23.0742C52.5716 22.3906 50.6836 21.9349 48.7305 21.707C46.8099 21.4466 44.9219 21.3164 43.0664 21.3164C40.4622 21.3164 38.0208 21.6745 35.7422 22.3906C33.4961 23.0742 31.5267 24.0996 29.834 25.4668C28.1738 26.834 26.8555 28.543 25.8789 30.5938C24.9023 32.6445 24.4141 35.0208 24.4141 37.7227C24.4141 39.4154 24.6419 41.0592 25.0977 42.6543C25.5534 44.2168 26.237 45.6165 27.1484 46.8535C28.0924 48.0579 29.248 49.0345 30.6152 49.7832C32.015 50.5319 33.6263 50.9062 35.4492 50.9062C36.6862 50.9062 37.9557 50.7435 39.2578 50.418C40.5924 50.0924 41.7318 49.5391 42.6758 48.7578L42.8711 44.9492L31.1523 44.7539L31.7383 30.1055C36.5885 29.9427 41.4388 29.8125 46.2891 29.7148C51.1393 29.5846 56.0221 29.4219 60.9375 29.2266L61.5234 61.0625ZM125.635 34.5977C125.635 37.5599 125.293 40.4245 124.609 43.1914C123.926 45.9258 122.933 48.4974 121.631 50.9062C120.361 53.3151 118.799 55.5286 116.943 57.5469C115.12 59.5326 113.053 61.2415 110.742 62.6738C108.464 64.1061 105.973 65.2292 103.271 66.043C100.57 66.8242 97.7051 67.2148 94.6777 67.2148C91.748 67.2148 88.9486 66.8405 86.2793 66.0918C83.6426 65.3431 81.1686 64.3014 78.8574 62.9668C76.5462 61.5996 74.4466 59.972 72.5586 58.084C70.7031 56.1634 69.1081 54.0475 67.7734 51.7363C66.4714 49.3926 65.446 46.8861 64.6973 44.2168C63.9811 41.5475 63.623 38.7643 63.623 35.8672C63.623 33.0352 63.9648 30.2682 64.6484 27.5664C65.332 24.832 66.3086 22.2604 67.5781 19.8516C68.8802 17.4427 70.4427 15.2292 72.2656 13.2109C74.0885 11.1927 76.123 9.45117 78.3691 7.98633C80.6478 6.52148 83.1055 5.38216 85.7422 4.56836C88.3789 3.75456 91.1621 3.34766 94.0918 3.34766C98.8118 3.34766 103.109 4.08008 106.982 5.54492C110.889 7.00977 114.209 9.10938 116.943 11.8438C119.71 14.5456 121.842 17.8333 123.34 21.707C124.87 25.5482 125.635 29.8451 125.635 34.5977ZM103.955 35.8672C103.955 34.4674 103.743 33.1165 103.32 31.8145C102.93 30.4798 102.344 29.3079 101.562 28.2988C100.781 27.2572 99.8047 26.4271 98.6328 25.8086C97.4935 25.1576 96.1751 24.832 94.6777 24.832C93.1478 24.832 91.7806 25.1087 90.5762 25.6621C89.3717 26.2155 88.3301 26.9805 87.4512 27.957C86.6048 28.901 85.9538 30.0241 85.498 31.3262C85.0423 32.5957 84.8145 33.9466 84.8145 35.3789C84.8145 36.7461 85.0098 38.1133 85.4004 39.4805C85.791 40.8477 86.377 42.0846 87.1582 43.1914C87.9395 44.2982 88.8997 45.1934 90.0391 45.877C91.2109 46.5605 92.5618 46.9023 94.0918 46.9023C95.6217 46.9023 96.9889 46.6094 98.1934 46.0234C99.4303 45.4049 100.472 44.5911 101.318 43.582C102.165 42.5404 102.816 41.3522 103.271 40.0176C103.727 38.6829 103.955 37.2995 103.955 35.8672ZM189.404 34.5977C189.404 37.5599 189.062 40.4245 188.379 43.1914C187.695 45.9258 186.702 48.4974 185.4 50.9062C184.131 53.3151 182.568 55.5286 180.713 57.5469C178.89 59.5326 176.823 61.2415 174.512 62.6738C172.233 64.1061 169.743 65.2292 167.041 66.043C164.339 66.8242 161.475 67.2148 158.447 67.2148C155.518 67.2148 152.718 66.8405 150.049 66.0918C147.412 65.3431 144.938 64.3014 142.627 62.9668C140.316 61.5996 138.216 59.972 136.328 58.084C134.473 56.1634 132.878 54.0475 131.543 51.7363C130.241 49.3926 129.215 46.8861 128.467 44.2168C127.751 41.5475 127.393 38.7643 127.393 35.8672C127.393 33.0352 127.734 30.2682 128.418 27.5664C129.102 24.832 130.078 22.2604 131.348 19.8516C132.65 17.4427 134.212 15.2292 136.035 13.2109C137.858 11.1927 139.893 9.45117 142.139 7.98633C144.417 6.52148 146.875 5.38216 149.512 4.56836C152.148 3.75456 154.932 3.34766 157.861 3.34766C162.581 3.34766 166.878 4.08008 170.752 5.54492C174.658 7.00977 177.979 9.10938 180.713 11.8438C183.48 14.5456 185.612 17.8333 187.109 21.707C188.639 25.5482 189.404 29.8451 189.404 34.5977ZM167.725 35.8672C167.725 34.4674 167.513 33.1165 167.09 31.8145C166.699 30.4798 166.113 29.3079 165.332 28.2988C164.551 27.2572 163.574 26.4271 162.402 25.8086C161.263 25.1576 159.945 24.832 158.447 24.832C156.917 24.832 155.55 25.1087 154.346 25.6621C153.141 26.2155 152.1 26.9805 151.221 27.957C150.374 28.901 149.723 30.0241 149.268 31.3262C148.812 32.5957 148.584 33.9466 148.584 35.3789C148.584 36.7461 148.779 38.1133 149.17 39.4805C149.561 40.8477 150.146 42.0846 150.928 43.1914C151.709 44.2982 152.669 45.1934 153.809 45.877C154.98 46.5605 156.331 46.9023 157.861 46.9023C159.391 46.9023 160.758 46.6094 161.963 46.0234C163.2 45.4049 164.242 44.5911 165.088 43.582C165.934 42.5404 166.585 41.3522 167.041 40.0176C167.497 38.6829 167.725 37.2995 167.725 35.8672ZM248.145 33.2305C248.145 37.7878 247.559 41.873 246.387 45.4863C245.215 49.0996 243.571 52.306 241.455 55.1055C239.372 57.8724 236.882 60.2324 233.984 62.1855C231.087 64.1387 227.913 65.75 224.463 67.0195C221.012 68.2565 217.334 69.168 213.428 69.7539C209.554 70.3398 205.566 70.6328 201.465 70.6328C199.935 70.6328 198.438 70.6003 196.973 70.5352C195.508 70.4375 194.01 70.3073 192.48 70.1445L193.848 6.57031C197.428 5.43099 201.107 4.63346 204.883 4.17773C208.691 3.68945 212.467 3.44531 216.211 3.44531C220.866 3.44531 225.146 4.09635 229.053 5.39844C232.959 6.70052 236.328 8.62109 239.16 11.1602C241.992 13.6667 244.189 16.7754 245.752 20.4863C247.347 24.1647 248.145 28.4128 248.145 33.2305ZM214.941 51.9805C216.895 51.6875 218.669 51.1016 220.264 50.2227C221.891 49.3112 223.291 48.2044 224.463 46.9023C225.635 45.5677 226.53 44.0703 227.148 42.4102C227.799 40.7174 228.125 38.9271 228.125 37.0391C228.125 35.1836 227.93 33.4583 227.539 31.8633C227.148 30.2357 226.514 28.8359 225.635 27.6641C224.756 26.4596 223.6 25.5156 222.168 24.832C220.736 24.1159 218.978 23.7253 216.895 23.6602L214.941 51.9805Z" fill="url(#paint0_linear_203_27)" />
                                    <defs>
                                        <linearGradient id="paint0_linear_203_27" x1="132" y1="2" x2="132" y2="105" gradientUnits="userSpaceOnUse">
                                            <stop stop-color="#DEE8E2" />
                                            <stop offset="0.0001" stop-color="#0D52F2" stop-opacity="0.980033" />
                                            <stop offset="0.0002" stop-color="#0D52F2" stop-opacity="0.973283" />
                                            <stop offset="0.0003" stop-color="#EB00FF" stop-opacity="0.966674" />
                                            <stop offset="0.35625" stop-color="#E259EE" stop-opacity="0.55" />
                                            <stop offset="0.955208" stop-color="#E0E2DF" stop-opacity="0.634534" />
                                        </linearGradient>
                                    </defs>
                                </svg>
                            </div>

                            :
                            playResult === "soso" ?
                                <div className="result-logo">
                                    <svg width="233" height="72" viewBox="0 0 233 72" fill="none" xmlns="http://www.w3.org/2000/svg">
                                        <path d="M51.5391 46C51.5391 50.4596 50.7578 54.3171 49.1953 57.5723C47.6654 60.7949 45.5495 63.4642 42.8477 65.5801C40.1784 67.6634 37.0371 69.2096 33.4238 70.2188C29.8431 71.1953 25.9857 71.6836 21.8516 71.6836C20.2565 71.6836 18.4987 71.472 16.5781 71.0488C14.6901 70.6257 12.7695 70.1048 10.8164 69.4863C8.86328 68.8353 6.97526 68.1517 5.15234 67.4355C3.36198 66.6868 1.76693 65.987 0.367188 65.3359L2.51562 46.1953C5.28255 47.8555 8.34245 49.125 11.6953 50.0039C15.0807 50.8503 18.401 51.2734 21.6562 51.2734C22.2747 51.2734 23.0234 51.2572 23.9023 51.2246C24.7812 51.1595 25.6113 51.013 26.3926 50.7852C27.2064 50.5247 27.89 50.1504 28.4434 49.6621C28.9967 49.1738 29.2734 48.474 29.2734 47.5625C29.2734 46.944 29.0781 46.4232 28.6875 46C28.2969 45.5443 27.7923 45.1862 27.1738 44.9258C26.5553 44.6328 25.8555 44.4212 25.0742 44.291C24.293 44.1283 23.528 44.0143 22.7793 43.9492C22.0306 43.8841 21.3307 43.8516 20.6797 43.8516C20.0286 43.8516 19.5078 43.8516 19.1172 43.8516C16.3177 43.8516 13.7624 43.4121 11.4512 42.5332C9.17253 41.6543 7.20312 40.4173 5.54297 38.8223C3.91536 37.1947 2.64583 35.2415 1.73438 32.9629C0.822917 30.6517 0.367188 28.0964 0.367188 25.2969C0.367188 21.4557 1.13216 18.0215 2.66211 14.9941C4.22461 11.9342 6.29167 9.34635 8.86328 7.23047C11.4674 5.08203 14.446 3.43815 17.7988 2.29883C21.1517 1.15951 24.6185 0.589844 28.1992 0.589844C29.7943 0.589844 31.4382 0.654948 33.1309 0.785156C34.8236 0.882812 36.5 1.07812 38.1602 1.37109C39.8529 1.66406 41.4967 2.03841 43.0918 2.49414C44.6868 2.94987 46.2005 3.51953 47.6328 4.20312L45.7773 24.0273C43.5638 23.2786 41.2363 22.6602 38.7949 22.1719C36.3861 21.651 34.026 21.3906 31.7148 21.3906C31.2917 21.3906 30.7383 21.4069 30.0547 21.4395C29.4036 21.4395 28.7038 21.4883 27.9551 21.5859C27.2389 21.651 26.5065 21.765 25.7578 21.9277C25.0091 22.0905 24.3418 22.3184 23.7559 22.6113C23.1699 22.8717 22.6979 23.2298 22.3398 23.6855C21.9818 24.1413 21.819 24.6784 21.8516 25.2969C21.8841 26.013 22.1445 26.5664 22.6328 26.957C23.1536 27.3477 23.8047 27.6406 24.5859 27.8359C25.3997 27.9987 26.2786 28.0801 27.2227 28.0801C28.1992 28.0801 29.1595 28.0638 30.1035 28.0312C31.0475 27.9661 31.9264 27.9173 32.7402 27.8848C33.554 27.8197 34.2214 27.8034 34.7422 27.8359C37.3464 27.9987 39.6901 28.5521 41.7734 29.4961C43.8568 30.4401 45.6146 31.6934 47.0469 33.2559C48.5117 34.8184 49.6185 36.6901 50.3672 38.8711C51.1484 41.0195 51.5391 43.3958 51.5391 46ZM115.064 33.5977C115.064 36.5599 114.723 39.4245 114.039 42.1914C113.355 44.9258 112.363 47.4974 111.061 49.9062C109.791 52.3151 108.229 54.5286 106.373 56.5469C104.55 58.5326 102.483 60.2415 100.172 61.6738C97.8932 63.1061 95.403 64.2292 92.7012 65.043C89.9993 65.8242 87.1348 66.2148 84.1074 66.2148C81.1777 66.2148 78.3783 65.8405 75.709 65.0918C73.0723 64.3431 70.5983 63.3014 68.2871 61.9668C65.9759 60.5996 63.8763 58.972 61.9883 57.084C60.1328 55.1634 58.5378 53.0475 57.2031 50.7363C55.901 48.3926 54.8757 45.8861 54.127 43.2168C53.4108 40.5475 53.0527 37.7643 53.0527 34.8672C53.0527 32.0352 53.3945 29.2682 54.0781 26.5664C54.7617 23.832 55.7383 21.2604 57.0078 18.8516C58.3099 16.4427 59.8724 14.2292 61.6953 12.2109C63.5182 10.1927 65.5527 8.45117 67.7988 6.98633C70.0775 5.52148 72.5352 4.38216 75.1719 3.56836C77.8086 2.75456 80.5918 2.34766 83.5215 2.34766C88.2415 2.34766 92.5384 3.08008 96.4121 4.54492C100.318 6.00977 103.639 8.10938 106.373 10.8438C109.14 13.5456 111.272 16.8333 112.77 20.707C114.299 24.5482 115.064 28.8451 115.064 33.5977ZM93.3848 34.8672C93.3848 33.4674 93.1732 32.1165 92.75 30.8145C92.3594 29.4798 91.7734 28.3079 90.9922 27.2988C90.2109 26.2572 89.2344 25.4271 88.0625 24.8086C86.9232 24.1576 85.6048 23.832 84.1074 23.832C82.5775 23.832 81.2103 24.1087 80.0059 24.6621C78.8014 25.2155 77.7598 25.9805 76.8809 26.957C76.0345 27.901 75.3835 29.0241 74.9277 30.3262C74.472 31.5957 74.2441 32.9466 74.2441 34.3789C74.2441 35.7461 74.4395 37.1133 74.8301 38.4805C75.2207 39.8477 75.8066 41.0846 76.5879 42.1914C77.3691 43.2982 78.3294 44.1934 79.4688 44.877C80.6406 45.5605 81.9915 45.9023 83.5215 45.9023C85.0514 45.9023 86.4186 45.6094 87.623 45.0234C88.86 44.4049 89.9017 43.5911 90.748 42.582C91.5944 41.5404 92.2454 40.3522 92.7012 39.0176C93.1569 37.6829 93.3848 36.2995 93.3848 34.8672ZM168.531 46C168.531 50.4596 167.75 54.3171 166.188 57.5723C164.658 60.7949 162.542 63.4642 159.84 65.5801C157.171 67.6634 154.029 69.2096 150.416 70.2188C146.835 71.1953 142.978 71.6836 138.844 71.6836C137.249 71.6836 135.491 71.472 133.57 71.0488C131.682 70.6257 129.762 70.1048 127.809 69.4863C125.855 68.8353 123.967 68.1517 122.145 67.4355C120.354 66.6868 118.759 65.987 117.359 65.3359L119.508 46.1953C122.275 47.8555 125.335 49.125 128.688 50.0039C132.073 50.8503 135.393 51.2734 138.648 51.2734C139.267 51.2734 140.016 51.2572 140.895 51.2246C141.773 51.1595 142.604 51.013 143.385 50.7852C144.199 50.5247 144.882 50.1504 145.436 49.6621C145.989 49.1738 146.266 48.474 146.266 47.5625C146.266 46.944 146.07 46.4232 145.68 46C145.289 45.5443 144.785 45.1862 144.166 44.9258C143.548 44.6328 142.848 44.4212 142.066 44.291C141.285 44.1283 140.52 44.0143 139.771 43.9492C139.023 43.8841 138.323 43.8516 137.672 43.8516C137.021 43.8516 136.5 43.8516 136.109 43.8516C133.31 43.8516 130.755 43.4121 128.443 42.5332C126.165 41.6543 124.195 40.4173 122.535 38.8223C120.908 37.1947 119.638 35.2415 118.727 32.9629C117.815 30.6517 117.359 28.0964 117.359 25.2969C117.359 21.4557 118.124 18.0215 119.654 14.9941C121.217 11.9342 123.284 9.34635 125.855 7.23047C128.46 5.08203 131.438 3.43815 134.791 2.29883C138.144 1.15951 141.611 0.589844 145.191 0.589844C146.786 0.589844 148.43 0.654948 150.123 0.785156C151.816 0.882812 153.492 1.07812 155.152 1.37109C156.845 1.66406 158.489 2.03841 160.084 2.49414C161.679 2.94987 163.193 3.51953 164.625 4.20312L162.77 24.0273C160.556 23.2786 158.229 22.6602 155.787 22.1719C153.378 21.651 151.018 21.3906 148.707 21.3906C148.284 21.3906 147.73 21.4069 147.047 21.4395C146.396 21.4395 145.696 21.4883 144.947 21.5859C144.231 21.651 143.499 21.765 142.75 21.9277C142.001 22.0905 141.334 22.3184 140.748 22.6113C140.162 22.8717 139.69 23.2298 139.332 23.6855C138.974 24.1413 138.811 24.6784 138.844 25.2969C138.876 26.013 139.137 26.5664 139.625 26.957C140.146 27.3477 140.797 27.6406 141.578 27.8359C142.392 27.9987 143.271 28.0801 144.215 28.0801C145.191 28.0801 146.152 28.0638 147.096 28.0312C148.04 27.9661 148.919 27.9173 149.732 27.8848C150.546 27.8197 151.214 27.8034 151.734 27.8359C154.339 27.9987 156.682 28.5521 158.766 29.4961C160.849 30.4401 162.607 31.6934 164.039 33.2559C165.504 34.8184 166.611 36.6901 167.359 38.8711C168.141 41.0195 168.531 43.3958 168.531 46ZM232.057 33.5977C232.057 36.5599 231.715 39.4245 231.031 42.1914C230.348 44.9258 229.355 47.4974 228.053 49.9062C226.783 52.3151 225.221 54.5286 223.365 56.5469C221.542 58.5326 219.475 60.2415 217.164 61.6738C214.885 63.1061 212.395 64.2292 209.693 65.043C206.992 65.8242 204.127 66.2148 201.1 66.2148C198.17 66.2148 195.37 65.8405 192.701 65.0918C190.064 64.3431 187.59 63.3014 185.279 61.9668C182.968 60.5996 180.868 58.972 178.98 57.084C177.125 55.1634 175.53 53.0475 174.195 50.7363C172.893 48.3926 171.868 45.8861 171.119 43.2168C170.403 40.5475 170.045 37.7643 170.045 34.8672C170.045 32.0352 170.387 29.2682 171.07 26.5664C171.754 23.832 172.73 21.2604 174 18.8516C175.302 16.4427 176.865 14.2292 178.688 12.2109C180.51 10.1927 182.545 8.45117 184.791 6.98633C187.07 5.52148 189.527 4.38216 192.164 3.56836C194.801 2.75456 197.584 2.34766 200.514 2.34766C205.234 2.34766 209.531 3.08008 213.404 4.54492C217.311 6.00977 220.631 8.10938 223.365 10.8438C226.132 13.5456 228.264 16.8333 229.762 20.707C231.292 24.5482 232.057 28.8451 232.057 33.5977ZM210.377 34.8672C210.377 33.4674 210.165 32.1165 209.742 30.8145C209.352 29.4798 208.766 28.3079 207.984 27.2988C207.203 26.2572 206.227 25.4271 205.055 24.8086C203.915 24.1576 202.597 23.832 201.1 23.832C199.57 23.832 198.202 24.1087 196.998 24.6621C195.794 25.2155 194.752 25.9805 193.873 26.957C193.027 27.901 192.376 29.0241 191.92 30.3262C191.464 31.5957 191.236 32.9466 191.236 34.3789C191.236 35.7461 191.432 37.1133 191.822 38.4805C192.213 39.8477 192.799 41.0846 193.58 42.1914C194.361 43.2982 195.322 44.1934 196.461 44.877C197.633 45.5605 198.984 45.9023 200.514 45.9023C202.044 45.9023 203.411 45.6094 204.615 45.0234C205.852 44.4049 206.894 43.5911 207.74 42.582C208.587 41.5404 209.238 40.3522 209.693 39.0176C210.149 37.6829 210.377 36.2995 210.377 34.8672Z" fill="url(#paint0_linear_203_23)" />
                                        <defs>
                                            <linearGradient id="paint0_linear_203_23" x1="131" y1="1" x2="131" y2="104" gradientUnits="userSpaceOnUse">
                                                <stop stop-color="#DEE8E2" />
                                                <stop offset="0.0001" stop-color="#0D52F2" stop-opacity="0.980033" />
                                                <stop offset="0.0002" stop-color="#0D52F2" stop-opacity="0.973283" />
                                                <stop offset="0.0003" stop-color="#EB00FF" stop-opacity="0.966674" />
                                                <stop offset="0.35625" stop-color="#E259EE" stop-opacity="0.55" />
                                                <stop offset="0.955208" stop-color="#E0E2DF" stop-opacity="0.634534" />
                                            </linearGradient>
                                        </defs>
                                    </svg>

                                </div>
                                :
                                playResult === "bad" ?
                                    <div className="result-logo">
                                        <svg width="178" height="74" viewBox="0 0 178 74" fill="none" xmlns="http://www.w3.org/2000/svg">
                                            <path d="M57.3262 54.6172C57.3262 57.319 56.8216 59.6465 55.8125 61.5996C54.8359 63.5527 53.5176 65.2129 51.8574 66.5801C50.1973 67.9473 48.2767 69.0703 46.0957 69.9492C43.9473 70.7956 41.7012 71.4629 39.3574 71.9512C37.0137 72.4395 34.6536 72.765 32.2773 72.9277C29.9336 73.0905 27.7363 73.1719 25.6855 73.1719C23.6673 73.1719 21.5677 73.0905 19.3867 72.9277C17.2383 72.7975 15.0898 72.5534 12.9414 72.1953C10.793 71.8372 8.67708 71.3652 6.59375 70.7793C4.51042 70.1608 2.54102 69.3958 0.685547 68.4844L0.587891 5.59375C2.41081 4.84505 4.34766 4.17773 6.39844 3.5918C8.44922 2.97331 10.5326 2.46875 12.6484 2.07812C14.7969 1.65495 16.929 1.3457 19.0449 1.15039C21.1608 0.922526 23.2116 0.808594 25.1973 0.808594C27.5085 0.808594 29.8359 0.955078 32.1797 1.24805C34.556 1.54102 36.8346 2.0293 39.0156 2.71289C41.1966 3.39648 43.2311 4.29167 45.1191 5.39844C47.0072 6.47266 48.651 7.80729 50.0508 9.40234C51.4831 10.9974 52.5898 12.8529 53.3711 14.9688C54.1849 17.0846 54.5918 19.526 54.5918 22.293C54.5918 24.1159 54.3314 25.8249 53.8105 27.4199C53.2897 29.015 52.541 30.4473 51.5645 31.7168C50.5879 32.9863 49.3997 34.0768 48 34.9883C46.6003 35.8672 45.0215 36.5182 43.2637 36.9414C45.3796 37.4948 47.3001 38.3086 49.0254 39.3828C50.7832 40.457 52.2643 41.7591 53.4688 43.2891C54.7057 44.819 55.6497 46.5443 56.3008 48.4648C56.9844 50.3854 57.3262 52.4362 57.3262 54.6172ZM32.5215 26.7852C32.5215 25.8411 32.3262 25.0436 31.9355 24.3926C31.5449 23.709 31.0566 23.1556 30.4707 22.7324C29.9173 22.3092 29.2988 22 28.6152 21.8047C27.9642 21.6094 27.3783 21.5117 26.8574 21.5117C26.2064 21.5117 25.5716 21.6094 24.9531 21.8047C24.3346 22 23.7324 22.2279 23.1465 22.4883L22.8535 33.3281C23.6022 33.3281 24.5299 33.263 25.6367 33.1328C26.7435 33.0026 27.8014 32.7259 28.8105 32.3027C29.8522 31.847 30.7311 31.196 31.4473 30.3496C32.1634 29.4707 32.5215 28.2826 32.5215 26.7852ZM25.5879 42.7031C24.6439 42.7031 23.7324 42.8008 22.8535 42.9961L22.6582 54.1289C23.1465 54.2266 23.6185 54.3242 24.0742 54.4219C24.5625 54.487 25.0345 54.5195 25.4902 54.5195C26.1738 54.5195 26.9225 54.4544 27.7363 54.3242C28.5501 54.1615 29.3151 53.8685 30.0312 53.4453C30.7474 52.9896 31.3333 52.3874 31.7891 51.6387C32.2773 50.89 32.5215 49.8971 32.5215 48.6602C32.5215 47.4232 32.2773 46.4141 31.7891 45.6328C31.3333 44.8516 30.7637 44.2493 30.0801 43.8262C29.3965 43.3704 28.6478 43.0775 27.834 42.9473C27.0202 42.7845 26.2715 42.7031 25.5879 42.7031ZM121.535 67.8008L97.3164 70.9258L94.3867 59.8906H83.2539L80.8125 70.9258L55.9102 68.4844L75.3438 3.44531L102.492 2.07812L121.535 67.8008ZM92.6289 44.9492L88.918 27.957L85.3047 44.9492H92.6289ZM177.883 33.2305C177.883 37.7878 177.297 41.873 176.125 45.4863C174.953 49.0996 173.309 52.306 171.193 55.1055C169.11 57.8724 166.62 60.2324 163.723 62.1855C160.826 64.1387 157.652 65.75 154.201 67.0195C150.751 68.2565 147.072 69.168 143.166 69.7539C139.292 70.3398 135.305 70.6328 131.203 70.6328C129.673 70.6328 128.176 70.6003 126.711 70.5352C125.246 70.4375 123.749 70.3073 122.219 70.1445L123.586 6.57031C127.167 5.43099 130.845 4.63346 134.621 4.17773C138.43 3.68945 142.206 3.44531 145.949 3.44531C150.604 3.44531 154.885 4.09635 158.791 5.39844C162.697 6.70052 166.066 8.62109 168.898 11.1602C171.73 13.6667 173.928 16.7754 175.49 20.4863C177.085 24.1647 177.883 28.4128 177.883 33.2305ZM144.68 51.9805C146.633 51.6875 148.407 51.1016 150.002 50.2227C151.63 49.3112 153.029 48.2044 154.201 46.9023C155.373 45.5677 156.268 44.0703 156.887 42.4102C157.538 40.7174 157.863 38.9271 157.863 37.0391C157.863 35.1836 157.668 33.4583 157.277 31.8633C156.887 30.2357 156.252 28.8359 155.373 27.6641C154.494 26.4596 153.339 25.5156 151.906 24.832C150.474 24.1159 148.716 23.7253 146.633 23.6602L144.68 51.9805Z" fill="url(#paint0_linear_203_25)" />
                                            <defs>
                                                <linearGradient id="paint0_linear_203_25" x1="130" y1="2" x2="130" y2="105" gradientUnits="userSpaceOnUse">
                                                    <stop stop-color="#F20D0D" />
                                                    <stop offset="0.0001" stop-color="#0D52F2" stop-opacity="0.980033" />
                                                    <stop offset="0.0002" stop-color="#0D52F2" stop-opacity="0.973283" />
                                                    <stop offset="0.0003" stop-color="#FF0202" stop-opacity="0.966674" />
                                                    <stop offset="0.9999" stop-color="#F68D8D" stop-opacity="0.0416667" />
                                                    <stop offset="1" stop-color="#0D52F2" stop-opacity="0" />
                                                    <stop offset="1" stop-color="#F20D0D" stop-opacity="0" />
                                                </linearGradient>
                                            </defs>
                                        </svg>

                                    </div>
                                    :
                                    playResult === "fail" ?
                                        <div className="result-logo">
                                            <svg width="179" height="71" viewBox="0 0 179 71" fill="none" xmlns="http://www.w3.org/2000/svg">
                                                <path d="M46.7402 1.56641C46.5449 4.6263 46.3659 7.65365 46.2031 10.6484C46.0404 13.6432 45.8288 16.6706 45.5684 19.7305L24.8652 20.8047V26.957H42.834L41.8574 42.0938L24.7676 42.582L24.8652 69.5352L0.158203 70.0234L1.7207 1.56641H46.7402ZM104.797 66.8008L80.5781 69.9258L77.6484 58.8906H66.5156L64.0742 69.9258L39.1719 67.4844L58.6055 2.44531L85.7539 1.07812L104.797 66.8008ZM75.8906 43.9492L72.1797 26.957L68.5664 43.9492H75.8906ZM131.311 2.25L127.893 68.7539L107.287 69.8281L105.334 3.61719L131.311 2.25ZM178.137 45.1211L176.086 66.8008L134.973 70.0234L136.535 0.296875H161.926L158.312 45.1211H178.137Z" fill="url(#paint0_linear_202_20)" />
                                                <defs>
                                                    <linearGradient id="paint0_linear_202_20" x1="88" y1="1" x2="88" y2="116" gradientUnits="userSpaceOnUse">
                                                        <stop stop-color="#F20D0D" />
                                                        <stop offset="0.0001" stop-color="#0D52F2" stop-opacity="0.980033" />
                                                        <stop offset="0.0002" stop-color="#0D52F2" stop-opacity="0.973283" />
                                                        <stop offset="0.0003" stop-color="#FF0202" stop-opacity="0.966674" />
                                                        <stop offset="0.9999" stop-color="#F68D8D" stop-opacity="0.0416667" />
                                                        <stop offset="1" stop-color="#0D52F2" stop-opacity="0" />
                                                        <stop offset="1" stop-color="#F20D0D" stop-opacity="0" />
                                                    </linearGradient>
                                                </defs>
                                            </svg>
                                        </div>
                                        :
                                        <></>
            }
        </>
    )
}

export default PracticeRoom;