import React, {useState, useEffect} from "react";
import SideMenu from "components/SideMenu";

import LeftArrowIcon from "/public/images/icon-left-arrow.svg"
import ReflectIcon from "/public/images/icon-reflect.svg"
import CameraIcon from "/public/images/icon-camera.svg"
import MicIcon from "/public/images/icon-mic.svg"
import MoreIcon from "/public/images/icon-more-dot.svg"
import PlayThumbnail from "/public/images/room-playlist-thumbnail.png"
import PlayIcon from "/public/images/icon-play.svg"
import ReflectHoverIcon from "/public/images/icon-hover-reflect.svg"
import MicHoverIcon from "/public/images/icon-hover-mic.svg"
import CameraHoverIcon from "/public/images/icon-hover-camera.svg"
import MoreDotHoverIcon from "/public/images/icon-hover-more-dot.svg"

import { useRecoilState } from "recoil";
import { LanguageState } from "states/states";

import Image from "next/image"
import Link from "next/link"

import { axiosMusic, axiosUser } from "apis/axios";
import { accessTokenState, refreshTokenState, idState } from "states/states";

const PracticeRoom = () => {
    const [lang, setLang] = useRecoilState(LanguageState);
    const [reflect, setReflect] = useState(false);
    const [mic, setMic] = useState(false);
    const [camera, setCamera] = useState(false);
    const [moredot, setMoredot] = useState(false);
    const [accessToken, setAccessToken] = useRecoilState(accessTokenState);
    const [refreshToken, setRefreshToken] = useRecoilState(refreshTokenState);
    const [id, setId] = useRecoilState(idState);
    const [musics, setMusics] = useState<any>();
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

        try{
            axiosUser.post('/auth',{
                id: id,
            },{
                headers:{
                    Authorization: `Bearer ${accessToken}`,
                    refreshToken: refreshToken,
                }
            }).then((data) => {
                if(data.data.message === "토큰 재발급 완료"){
                    setAccessToken(data.data.data.accessToken);
                    setRefreshToken(data.data.data.refreshToken);
                }
            })
        }catch(e){
            alert('시스템 에러, 관리자에게 문의하세요.');
        }

        axiosMusic.get("",{
            params:{
                keyword: "",
            },
            headers:{
                Authorization: `Bearer ${accessToken}`,
            }
        }).then((data) => {
            console.log(data);
            setMusics(data.data.data);
        })
    }, [])
    return(
        <>
            <div className="practiceroom-wrap">
                <SideMenu/>
                <div className="practice-video-wrap">
                    <div className="practice-title">
                        <div className="pre-icon">
                            <Link href="/"><Image src={LeftArrowIcon} alt=""/></Link>
                        </div>
                        <div className="room-title">
                            <h3>보이넥스트도어 - One and Only</h3>
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
                                <li><button className="exit-button">{lang==="en" ? "End Practice" : lang==="cn" ? "结束练习" : "연습 종료하기" }</button></li>
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
                <div className="musiclist">
                    <div className="musiclist-title">
                        <h3>{lang==="en" ? "List of practice rooms" : lang==="cn" ? "练习室列表" : "연습실 목록" }</h3>
                        <span>{musics?.length}</span>
                    </div>
                    <div className="musiclist-content">
                        <ul>
                            {musics?.map((music:any) => {
                                return(
                                    <li>
                                        <div className="flex-wrap">
                                            <div className="musiclist-content-thumbnail">
                                                <Image src={PlayThumbnail} alt=""/>
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
        </>
    )
}

export default PracticeRoom;