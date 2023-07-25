import React, {useState, useRef, useEffect, KeyboardEvent} from "react";
import socketIOClient from "socket.io-client";

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
    let myStream : any;
    let myPeerConnection : any;
    const [muteState, setMuteState] = useState(false);
    const [cameraState, setCameraState] = useState(false);

    const myVideoRef = useRef<HTMLVideoElement>(null);
    useEffect(() => {
        getMedia();
    })
    const getMedia = async () => {
        try{
            myStream = await navigator.mediaDevices.getUserMedia({
                audio: true,
                video: true,
            });
            if(myVideoRef.current != null){
                console.log(myStream);
                myVideoRef.current.srcObject = myStream;
                myVideoRef.current.play();
            }
        }catch(e){
            console.log(e);
        }
    }

    const handleMuteClick = () => {
        myStream?.getAudioTracks().forEach((track : any) => (track.enabled = !track.enabled));
        if(!muteState){
            setMuteState(true);
        }else{
            setMuteState(false);
        }
    }

    const handleCameraClick = () => {
        myStream?.getVideoTracks().forEach((track : any) => (track.enabled = !track.enabled));
        if(!cameraState){
            setCameraState(false);
        }else{
            setCameraState(true);
        }
    }


    //socket.io 통신 및 채팅방 구현
    const socket = socketIOClient("localhost:4002");
    const roomName = 1;
    socket.emit("enter_room", roomName);
    const [msg, setMsg] = useState('');
    const [msgList, setMsgList] = useState<any[]>([]);
    const inputChat = useRef(null);
    const chatContent = useRef(null);
    const sendMessage = () => {
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

    useEffect(() => {
        scrollToBottom();
        myPeerConnection = new RTCPeerConnection({
            iceServers: [
                {
                    urls: [
                        "stun:stun.l.google.com:19302",
                        "stun:stun1.l.google.com:19302",
                        "stun:stun2.l.google.com:19302",
                        "stun:stun3.l.google.com:19302",
                        "stun:stun4.l.google.com:19302",
                    ],
                },
            ],
        });
    })
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
                            <video src="" id="peerFace" playsInline ref={myVideoRef}></video>
                        </div>
                        <div className="yours-video">
                            <ul>
                                <li>
                                    <video src="" playsInline ></video>
                                </li>
                                <li>
                                    <video src=""></video>
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
                                <li onMouseEnter = {micHover} onMouseLeave = {micLeave} id="mute" onClick={handleMuteClick}>
                                    <button>
                                        {mic ? <Image src={MicHoverIcon} alt=""/> : <Image src={MicIcon} alt=""/>}
                                    </button>
                                </li>
                                <li><button className="exit-button">연습 종료하기</button></li>
                                <li onMouseEnter = {cameraHover} onMouseLeave = {cameraLeave} id="camera" onClick={handleCameraClick}>
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
                            <span><Image src={GroupIcon} alt=""/>5명</span>
                        </div>
                        <div className="participant-list-content">
                            <ul>
                                <li className="on">
                                        <Image src={ChatDefaultImg} alt=""/>
                                        <span>ME</span>
                                </li>
                                <li>
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
                                </li>
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