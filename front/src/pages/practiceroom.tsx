import SideMenu from "components/SideMenu";

import LeftArrowIcon from "/public/images/icon-left-arrow.svg"
import GroupIcon from "/public/images/icon-group.svg"
import PlusIcon from "/public/images/icon-plus.svg"
import ReflectIcon from "/public/images/icon-reflect.svg"
import CameraIcon from "/public/images/icon-camera.svg"
import MicIcon from "/public/images/icon-mic.svg"
import MoreIcon from "/public/images/icon-more-dot.svg"

import Image from "next/image"

const PracticeRoom = () => {
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
                                <li><button><Image src={ReflectIcon} alt=""/></button></li>
                                <li><button><Image src={MicIcon} alt=""/></button></li>
                                <li><button className="exit-button">연습 종료하기</button></li>
                                <li><button><Image src={CameraIcon} alt=""/></button></li>
                                <li><button><Image src={MoreIcon} alt=""/></button></li>
                            </ul>
                        </div>
                    </div>
                    
                </div>
                <div className="musiclist">
                    <div className="musiclist-title">
                        <h3>연습실 목록</h3>
                        <span>5</span>
                    </div>
                </div>
            </div>
        </>
    )
}

export default PracticeRoom;