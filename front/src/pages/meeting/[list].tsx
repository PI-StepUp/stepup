import Header from "components/Header";
import MainBanner from "components/MainBanner";
import SubNav from "components/subNav";
import Footer from "components/Footer";

import defaultMeetingProfileImg from "/public/images/default-meeting-profile.svg"

import Image from "next/image"

const MeetingList = () => {
    return (
        <>
            <Header/>
            <MainBanner/>
            <SubNav/>
            <div className="meeting-wrap">
                <div className="block-center-wrap">
                    <div className="geography-wrap">
                        <div className="geography-title">
                            <h3>
                                View the World<br/>
                                Random Play Dance
                            </h3>
                            <span>KR : 한국</span>
                        </div>
                    </div>
                    <div className="meeting-content-wrap">
                        <ul>
                            <li>
                                <div className="user-wrap">
                                    <Image src={defaultMeetingProfileImg} alt=""/>
                                    <p>Nickname</p>
                                </div>
                                <div className="meeting-content">
                                    <p>16일 일요일! 역삼역 이프푸 찍으실 분 구합니다.</p>
                                    <span>같이 갈 사람 제의하기</span>
                                </div>
                            </li>
                            <li>
                                <div className="user-wrap">
                                    <Image src={defaultMeetingProfileImg} alt=""/>
                                    <p>Nickname</p>
                                </div>
                                <div className="meeting-content">
                                    <p>16일 일요일! 역삼역 이프푸 찍으실 분 구합니다.</p>
                                    <span>같이 갈 사람 제의하기</span>
                                </div>
                            </li>
                            <li>
                                <div className="user-wrap">
                                    <Image src={defaultMeetingProfileImg} alt=""/>
                                    <p>Nickname</p>
                                </div>
                                <div className="meeting-content">
                                    <p>16일 일요일! 역삼역 이프푸 찍으실 분 구합니다.</p>
                                    <span>같이 갈 사람 제의하기</span>
                                </div>
                            </li>
                            <li>
                                <div className="user-wrap">
                                    <Image src={defaultMeetingProfileImg} alt=""/>
                                    <p>Nickname</p>
                                </div>
                                <div className="meeting-content">
                                    <p>16일 일요일! 역삼역 이프푸 찍으실 분 구합니다.</p>
                                    <span>같이 갈 사람 제의하기</span>
                                </div>
                            </li>
                            <li>
                                <div className="user-wrap">
                                    <Image src={defaultMeetingProfileImg} alt=""/>
                                    <p>Nickname</p>
                                </div>
                                <div className="meeting-content">
                                    <p>16일 일요일! 역삼역 이프푸 찍으실 분 구합니다.</p>
                                    <span>같이 갈 사람 제의하기</span>
                                </div>
                            </li>
                            <li>
                                <div className="user-wrap">
                                    <Image src={defaultMeetingProfileImg} alt=""/>
                                    <p>Nickname</p>
                                </div>
                                <div className="meeting-content">
                                    <p>16일 일요일! 역삼역 이프푸 찍으실 분 구합니다.</p>
                                    <span>같이 갈 사람 제의하기</span>
                                </div>
                            </li>
                            <li>
                                <div className="user-wrap">
                                    <Image src={defaultMeetingProfileImg} alt=""/>
                                    <p>Nickname</p>
                                </div>
                                <div className="meeting-content">
                                    <p>16일 일요일! 역삼역 이프푸 찍으실 분 구합니다.</p>
                                    <span>같이 갈 사람 제의하기</span>
                                </div>
                            </li>
                            <li>
                                <div className="user-wrap">
                                    <Image src={defaultMeetingProfileImg} alt=""/>
                                    <p>Nickname</p>
                                </div>
                                <div className="meeting-content">
                                    <p>16일 일요일! 역삼역 이프푸 찍으실 분 구합니다.</p>
                                    <span>같이 갈 사람 제의하기</span>
                                </div>
                            </li>
                            <li>
                                <div className="user-wrap">
                                    <Image src={defaultMeetingProfileImg} alt=""/>
                                    <p>Nickname</p>
                                </div>
                                <div className="meeting-content">
                                    <p>16일 일요일! 역삼역 이프푸 찍으실 분 구합니다.</p>
                                    <span>같이 갈 사람 제의하기</span>
                                </div>
                            </li>
                        </ul>
                    </div>
                    <div className="pagination">
                        <ul>
                            <li>1</li>
                            <li>2</li>
                            <li>3</li>
                            <li>4</li>
                            <li>5</li>
                            <li>6</li>
                            <li>7</li>
                            <li>8</li>
                        </ul>
                    </div>
                </div>
            </div>
            <Footer/>
        </>
    )
}

export default MeetingList;