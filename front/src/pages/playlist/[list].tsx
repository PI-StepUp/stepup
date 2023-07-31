import Header from "components/Header";
import MainBanner from "components/MainBanner";
import Footer from "components/Footer";
import LanguageButton from "components/LanguageButton";

import Image from "next/image";
import Link from "next/link";
import DefaultProfileImage from "/public/images/playlist-default-profile-img.svg";

import { useRecoilState } from "recoil";
import { LanguageState } from "states/states";

const PlayList = () => {
    const [lang, setLang] = useRecoilState(LanguageState);
    return (
        <>
            <Header/>
            <MainBanner/>
            <div className="playlist-wrap">
                <div className="playlist-title">
                    <span>THE NEW STEPUP’S PLAYLIST</span>
                    <h3>
                        {lang==="en" ? "STEPUP's new playlist" : lang==="cn" ? "STEPUP的新歌单" : "STEPUP의 새로운 플레이리스트" }<br/>
                        {lang==="en" ? "Enjoy it with more songs" : lang==="cn" ? "用更多的歌曲来享受吧" : "더 많은 곡으로 즐기세요" }
                    </h3>
                </div>
                <div className="playlist-create-button-wrap">
                    <button><Link href="/playlist/create">{lang==="en" ? "Request" : lang==="cn" ? "申请新曲" : "신곡 신청하기" }</Link></button>
                </div>
                <div className="playlist-content-wrap">
                    <ul>
                        <li>
                            <span>르세라핌</span>
                            <h4>이브, 프시케 그리고 푸른 수염의 아내</h4>
                            <p>이프푸 Boom, Boom, Boom 챌린지하고 싶어요. 플레이리스트에 넣어주세요.</p>
                            <div className="user-wrap">
                                <Image src={DefaultProfileImage} alt=""></Image>
                                <span>Nickname</span>
                            </div>
                        </li>
                        <li>
                            <span>르세라핌</span>
                            <h4>이브, 프시케 그리고 푸른 수염의 아내</h4>
                            <p>이프푸 Boom, Boom, Boom 챌린지하고 싶어요. 플레이리스트에 넣어주세요. 이프푸 Boom, Boom, Boom 챌린지하고 싶어요. 플레이리스트에 넣어주세요. 이프푸 Boom, Boom, Boom 챌린지하고 싶어요. 플레이리스트에 넣어주세요.</p>
                            <div className="user-wrap">
                                <Image src={DefaultProfileImage} alt=""></Image>
                                <span>Nickname</span>
                            </div>
                        </li>
                        <li>
                            <span>르세라핌</span>
                            <h4>이브, 프시케 그리고 푸른 수염의 아내</h4>
                            <p>이프푸 Boom, Boom, Boom 챌린지하고 싶어요. 플레이리스트에 넣어주세요.</p>
                            <div className="user-wrap">
                                <Image src={DefaultProfileImage} alt=""></Image>
                                <span>Nickname</span>
                            </div>
                        </li>
                        <li>
                            <span>르세라핌</span>
                            <h4>이브, 프시케 그리고 푸른 수염의 아내</h4>
                            <p>이프푸 Boom, Boom, Boom 챌린지하고 싶어요. 플레이리스트에 넣어주세요. 이프푸 Boom, Boom, Boom 챌린지하고 싶어요. 플레이리스트에 넣어주세요. 이프푸 Boom, Boom, Boom 챌린지하고 싶어요. 플레이리스트에 넣어주세요.</p>
                            <div className="user-wrap">
                                <Image src={DefaultProfileImage} alt=""></Image>
                                <span>Nickname</span>
                            </div>
                        </li>
                        <li>
                            <span>르세라핌</span>
                            <h4>이브, 프시케 그리고 푸른 수염의 아내</h4>
                            <p>이프푸 Boom, Boom, Boom 챌린지하고 싶어요. 플레이리스트에 넣어주세요.</p>
                            <div className="user-wrap">
                                <Image src={DefaultProfileImage} alt=""></Image>
                                <span>Nickname</span>
                            </div>
                        </li>
                        <li>
                            <span>르세라핌</span>
                            <h4>이브, 프시케 그리고 푸른 수염의 아내</h4>
                            <p>이프푸 Boom, Boom, Boom 챌린지하고 싶어요. 플레이리스트에 넣어주세요.</p>
                            <div className="user-wrap">
                                <Image src={DefaultProfileImage} alt=""></Image>
                                <span>Nickname</span>
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
            <LanguageButton/>
            <Footer/>
        </>
    )
}

export default PlayList;