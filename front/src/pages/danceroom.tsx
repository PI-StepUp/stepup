import React, {useState} from "react";

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
                            <video src=""></video>
                        </div>
                        <div className="yours-video">
                            <ul>
                                <li>
                                    <video src=""></video>
                                </li>
                                <li>
                                    <video src=""></video>
                                </li>
                                <li>
                                    <video src=""></video>
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
                                <li onMouseEnter = {micHover} onMouseLeave = {micLeave}>
                                    <button>
                                        {mic ? <Image src={MicHoverIcon} alt=""/> : <Image src={MicIcon} alt=""/>}
                                    </button>
                                </li>
                                <li><button className="exit-button">연습 종료하기</button></li>
                                <li onMouseEnter = {cameraHover} onMouseLeave = {cameraLeave}>
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
                            <div className="chat-read">
                                <ul>
                                    <li>
                                        <div className="chat-user-img">
                                            <Image src={ChatDefaultImg} alt=""/>
                                        </div>
                                        <div className="chat-user-msg">
                                            <span>김싸피</span>
                                            <p>웹기술 채팅내용입니다.</p>
                                        </div>
                                    </li>
                                    <li>
                                        <div className="chat-user-img">
                                            <Image src={ChatDefaultImg} alt=""/>
                                        </div>
                                        <div className="chat-user-msg">
                                            <span>김싸피</span>
                                            <p>웹기술 채팅내용입니다. 웹기술 채팅내용입니다. 웹기술 채팅내용입니다. 웹기술 채팅내용입니다.</p>
                                        </div>
                                    </li>
                                    <li>
                                        <div className="chat-user-img">
                                            <Image src={ChatDefaultImg} alt=""/>
                                        </div>
                                        <div className="chat-user-msg">
                                            <span>김싸피</span>
                                            <p>웹기술 채팅내용입니다.</p>
                                        </div>
                                    </li>
                                    <li>
                                        <div className="chat-user-img">
                                            <Image src={ChatDefaultImg} alt=""/>
                                        </div>
                                        <div className="chat-user-msg">
                                            <span>김싸피</span>
                                            <p>웹기술 채팅내용입니다.</p>
                                        </div>
                                    </li>
                                </ul>
                            </div>
                            <div className="chat-write">
                                <input type="text" placeholder="모두에게 전송"/>
                                <button><Image src={sendImg} alt=""/></button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </>
    )
}

export default DanceRoom;