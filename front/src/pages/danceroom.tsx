import React, {useState, useRef, useEffect, KeyboardEvent} from "react";
import socketIOClient, {Socket} from "socket.io-client";

import SideMenu from "components/SideMenu";

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

const DanceRoom = () => {

    const myName = "임시 이름";
    let peerNames : any = [];
    const [participantNum, setParticipantNum] = useState(1);
    const socketRef = useRef<Socket>();

    const [peer, setPeer] = useState('');
    const myVideoRef = useRef<HTMLVideoElement>(null);
    const otherVideoRef = useRef<HTMLVideoElement>(null);
    const peerRef = useRef<RTCPeerConnection>();

    //socket.io 통신 및 채팅방 구현
    const socket = socketIOClient("localhost:4002");
    const roomName = 1;
    socket.emit("enter_room", roomName);
    const [msg, setMsg] = useState('');
    const [msgList, setMsgList] = useState<any[]>([]);
    const inputChat = useRef(null);
    const chatContent = useRef(null);
    const sendMessage = () => {
        console.log(msg);
        socket.emit("send_message", msg);
        if(inputChat.current != null){
            setMsg("");
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
            socket.emit("send_message", msg);
            if(inputChat.current != null){
                setMsg("");
            }
            scrollToBottom();
        }
    };

    socket.on("message", (message) => {
        setMsgList([...msgList, message]);
    });


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

    const getMedia = async () => {
        try{
            const stream = await navigator.mediaDevices.getUserMedia({
                video: true,
                audio: true,
            });

            if(myVideoRef.current){
                myVideoRef.current.srcObject = stream;
                myVideoRef.current.play();
            }

            stream.getTracks().forEach((track) => {
                if(!peerRef.current){
                    return;
                }
                peerRef.current.addTrack(track, stream);
            });

            if(peerRef.current){
                peerRef.current.onicecandidate = (e) => {
                    if(e.candidate){
                        if(!socketRef.current){
                            return;
                        }

                        console.log("recv candidate");
                        socketRef.current.emit("candidate", e.candidate, [roomName, myName]);
                    }
                }
            }

            if(peerRef.current){
                peerRef.current.ontrack = (e) => {
                    if(otherVideoRef.current){
                        console.log("e", e);
                        otherVideoRef.current.srcObject = e.streams[0];
                        otherVideoRef.current.play();
                    }
                }
            }

            createOffer();
        }catch(e){
            console.error(e);
        }
    }

    const createOffer = async () => {
        console.log("create Offer");
        if (!(peerRef.current && socketRef.current)) {
          return;
        }
        try {
          // offer 생성
          const sdp = await peerRef.current.createOffer();
          // 자신의 sdp로 LocalDescription 설정
          peerRef.current.setLocalDescription(sdp);
          console.log("sent the offer");
          // offer 전달
          socketRef.current.emit("offer", sdp, roomName);
        } catch (e) {
          console.error(e);
        }
      };
      
      const createAnswer = async (sdp: RTCSessionDescription) => {
        // sdp : PeerA에게서 전달받은 offer
      
        console.log("createAnswer");
        if (!(peerRef.current && socketRef.current)) {
          return;
        }
    
        try {
          // PeerA가 전달해준 offer를 RemoteDescription에 등록해 줍시다.
          peerRef.current.setRemoteDescription(sdp);
          
          // answer생성해주고
          const answerSdp = await peerRef.current.createAnswer();
          
          // answer를 LocalDescription에 등록해 줍니다. (PeerB 기준)
          peerRef.current.setLocalDescription(answerSdp);
          console.log("sent the answer");
          socketRef.current.emit("answer", answerSdp, roomName);
        } catch (e) {
          console.error(e);
        }
      };

    useEffect(() => {
        scrollToBottom();
        socketRef.current = socketIOClient("localhost:4002");

        peerRef.current = new RTCPeerConnection({
            iceServers: [
                {
                    urls: "stun:stun.l.google.com:19302",
                },
            ],
        });

        socketRef.current.on("all_users", (allUsers: Array<{ id: string }>) => {
            if (allUsers.length > 0) {
              createOffer();
            }
          });
          
          // offer를 전달받은 PeerB만 해당됩니다
          // offer를 들고 만들어둔 answer 함수 실행
          socketRef.current.on("getOffer", (sdp: RTCSessionDescription) => {
            console.log("recv Offer");
            createAnswer(sdp);
          });
          
          // answer를 전달받을 PeerA만 해당됩니다.
          // answer를 전달받아 PeerA의 RemoteDescription에 등록
          socketRef.current.on("getAnswer", (sdp: RTCSessionDescription) => {
            console.log("recv Answer");
            if (!peerRef.current) {
              return;
            }
            if(peerRef.current){
                peerRef.current.setRemoteDescription(sdp);
            }
          });
          
          // 서로의 candidate를 전달받아 등록
          socketRef.current.on("getCandidate", async (candidate: RTCIceCandidate, peerName) => {
            if (!peerRef.current) {
              return;
            }
      
            await peerRef.current.addIceCandidate(candidate);
            peerNames.push(peerName);
            setPeer(peerName);
            setParticipantNum(participantNum + 1);
          });
          
          // 마운트시 해당 방의 roomName을 서버에 전달
          socketRef.current.emit("join_room", {
            room: roomName,
          });
      
          getMedia();
      
          return () => {
            // 언마운트시 socket disconnect
            if (socketRef.current) {
              socketRef.current.disconnect();
            }
            if (peerRef.current) {
              peerRef.current.close();
            }
          };

    }, []);

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
                        <div className="my-video">
                            <video src="" playsInline ref={myVideoRef}></video>
                        </div>
                        <div className="yours-video">
                            <ul>
                                <li>
                                    <video src="" playsInline ref={otherVideoRef}></video>
                                </li>
                            </ul>
                        </div>
                        <div className="control-wrap">
                            <ul>
                                <li onMouseEnter = {reflectHover} onMouseLeave = {reflectLeave}>
                                    <button>
                                        {reflect ? <Image src={ReflectHoverIcon} alt=""/> : <Image src={ReflectIcon} alt=""/>}
                                    </button>
                                </li>
                                <li onMouseEnter = {micHover} onMouseLeave = {micLeave} id="mute">
                                    <button>
                                        {mic ? <Image src={MicHoverIcon} alt=""/> : <Image src={MicIcon} alt=""/>}
                                    </button>
                                </li>
                                <li><button className="exit-button">연습 종료하기</button></li>
                                <li onMouseEnter = {cameraHover} onMouseLeave = {cameraLeave} id="camera">
                                    <button>
                                        {camera ? <Image src={CameraHoverIcon} alt=""/> : <Image src={CameraIcon} alt=""/>}
                                    </button>
                                </li>
                                <li onMouseEnter = {moreDotHover} onMouseLeave = {moreDotLeave}>
                                    <button>
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
                            <h3>참여자 목록</h3>
                            <span><Image src={GroupIcon} alt=""/>{participantNum}명</span>
                        </div>
                        <div className="participant-list-content">
                            <ul>
                                <li className="on">
                                        <Image src={ChatDefaultImg} alt=""/>
                                        <span>{myName}</span>
                                </li>
                                {
                                    peer.length > 0 ? <li>
                                    <Image src={ChatDefaultImg} alt=""/>
                                    <span>{peer}</span></li> : ""
                            
                                }
                                
                                {/* <li>
                                        <Image src={ChatDefaultImg} alt=""/>
                                        <span>이싸피</span>
                                </li>
                                <li>
                                        <Image src={ChatDefaultImg} alt=""/>
                                        <span>정싸피</span>
                                </li>
                                <li>
                                        <Image src={ChatDefaultImg} alt=""/>
                                        <span>윤싸피</span>
                                </li>
                                <li>
                                        <Image src={ChatDefaultImg} alt=""/>
                                        <span>최싸피</span>
                                </li> */}
                            </ul>
                        </div>
                    </div>
                    <div className="chat-wrap">
                        <div className="chat-title">
                            <h3>채팅하기</h3>
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
                                <input type="text" placeholder="모두에게 전송" ref={inputChat} onChange={(e) => setMsg(e.target.value)} value={msg} onKeyPress={handleKeyPress}/>
                                <button onClick={sendMessage}><Image src={sendImg} alt=""/></button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </>
    )
}

export default DanceRoom;