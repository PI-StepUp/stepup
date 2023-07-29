import Header from "components/Header";
import MainBanner from "components/MainBanner";
import SubNav from "components/subNav";
import Footer from "components/Footer";
import LanguageButton from "components/LanguageButton";

import defaultMeetingProfileImg from "/public/images/default-meeting-profile.svg"

import { useRecoilState } from "recoil";
import { LanguageState } from "states/states";

import Image from "next/image"
import Link from "next/link"

const MeetingList = () => {
    const [lang, setLang] = useRecoilState(LanguageState);
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
                    <div className="button-wrap">
                        <button><Link href="/meeting/create">{lang==="en" ? "CREATE" : lang==="cn" ? "撰写文章" : "글 작성하기" }</Link></button>
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
                                    <span>{lang==="en" ? "Offer someone to go with you" : lang==="cn" ? "提议一起去的人" : "같이 갈 사람 제의하기" }</span>
                                </div>
                            </li>
                            <li>
                                <div className="user-wrap">
                                    <Image src={defaultMeetingProfileImg} alt=""/>
                                    <p>Nickname</p>
                                </div>
                                <div className="meeting-content">
                                    <p>16일 일요일! 역삼역 이프푸 찍으실 분 구합니다.</p>
                                    <span>{lang==="en" ? "Offer someone to go with you" : lang==="cn" ? "提议一起去的人" : "같이 갈 사람 제의하기" }</span>
                                </div>
                            </li>
                            <li>
                                <div className="user-wrap">
                                    <Image src={defaultMeetingProfileImg} alt=""/>
                                    <p>Nickname</p>
                                </div>
                                <div className="meeting-content">
                                    <p>16일 일요일! 역삼역 이프푸 찍으실 분 구합니다.</p>
                                    <span>{lang==="en" ? "Offer someone to go with you" : lang==="cn" ? "提议一起去的人" : "같이 갈 사람 제의하기" }</span>
                                </div>
                            </li>
                            <li>
                                <div className="user-wrap">
                                    <Image src={defaultMeetingProfileImg} alt=""/>
                                    <p>Nickname</p>
                                </div>
                                <div className="meeting-content">
                                    <p>16일 일요일! 역삼역 이프푸 찍으실 분 구합니다.</p>
                                    <span>{lang==="en" ? "Offer someone to go with you" : lang==="cn" ? "提议一起去的人" : "같이 갈 사람 제의하기" }</span>
                                </div>
                            </li>
                            <li>
                                <div className="user-wrap">
                                    <Image src={defaultMeetingProfileImg} alt=""/>
                                    <p>Nickname</p>
                                </div>
                                <div className="meeting-content">
                                    <p>16일 일요일! 역삼역 이프푸 찍으실 분 구합니다.</p>
                                    <span>{lang==="en" ? "Offer someone to go with you" : lang==="cn" ? "提议一起去的人" : "같이 갈 사람 제의하기" }</span>
                                </div>
                            </li>
                            <li>
                                <div className="user-wrap">
                                    <Image src={defaultMeetingProfileImg} alt=""/>
                                    <p>Nickname</p>
                                </div>
                                <div className="meeting-content">
                                    <p>16일 일요일! 역삼역 이프푸 찍으실 분 구합니다.</p>
                                    <span>{lang==="en" ? "Offer someone to go with you" : lang==="cn" ? "提议一起去的人" : "같이 갈 사람 제의하기" }</span>
                                </div>
                            </li>
                            <li>
                                <div className="user-wrap">
                                    <Image src={defaultMeetingProfileImg} alt=""/>
                                    <p>Nickname</p>
                                </div>
                                <div className="meeting-content">
                                    <p>16일 일요일! 역삼역 이프푸 찍으실 분 구합니다.</p>
                                    <span>{lang==="en" ? "Offer someone to go with you" : lang==="cn" ? "提议一起去的人" : "같이 갈 사람 제의하기" }</span>
                                </div>
                            </li>
                            <li>
                                <div className="user-wrap">
                                    <Image src={defaultMeetingProfileImg} alt=""/>
                                    <p>Nickname</p>
                                </div>
                                <div className="meeting-content">
                                    <p>16일 일요일! 역삼역 이프푸 찍으실 분 구합니다.</p>
                                    <span>{lang==="en" ? "Offer someone to go with you" : lang==="cn" ? "提议一起去的人" : "같이 갈 사람 제의하기" }</span>
                                </div>
                            </li>
                            <li>
                                <div className="user-wrap">
                                    <Image src={defaultMeetingProfileImg} alt=""/>
                                    <p>Nickname</p>
                                </div>
                                <div className="meeting-content">
                                    <p>16일 일요일! 역삼역 이프푸 찍으실 분 구합니다.</p>
                                    <span>{lang==="en" ? "Offer someone to go with you" : lang==="cn" ? "提议一起去的人" : "같이 갈 사람 제의하기" }</span>
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
            <LanguageButton/>
            <Footer/>
        </>
    )
}

export default MeetingList;