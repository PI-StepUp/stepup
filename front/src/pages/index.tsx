import Link from "next/link"
import Image from "next/image"
import motionIcons from "/public/images/motion-icon.svg"
import personIcon from "/public/images/person-icon.svg"
import shareScreen from "/public/images/sharescreen-icon.svg"
import rankIcon from "/public/images/rank-icon.svg"
import musicNoteIcon from "/public/images/musicnote-icon.svg"
import randomplayAd from "/public/images/randomplay-ad-img.png"
import realtimeRandomPlay1 from "/public/images/realtimeRandomplayImg1.png"

const Index = () => {
    return(
        <>
            <header className="main-header">
                <div className="block-margin">
                    <div className="logo">
                        <h1><Link href="/">STEP UP</Link></h1>
                    </div>
                    <nav>
                        <ul>
                            <li><h2><Link href="/">랜덤플레이</Link></h2></li>
                            <li><h2><Link href="/notice/list">커뮤니티</Link></h2></li>
                            <li><h2><Link href="/">신곡신청</Link></h2></li>
                            <li><h2><Link href="/">연습실참가</Link></h2></li>
                        </ul>
                    </nav>
                    <div className="login-wrap">
                        <ul>
                            <li><Link href="/">로그인</Link></li>
                            <li><Link href="/">회원가입</Link></li>
                        </ul>
                    </div>
                </div>
            </header>
            <div className="main-banner">

            </div>
            <div className="main-info">
                <div className="flex-wrap">    
                    <h3>
                        KPOP에 필요한<br/>
                        모든 만남이<br/>
                        있는 곳
                    </h3>
                    <div className="main-info-text">
                        <p>
                            언제, 어디서든 장소에 구애받지 않고 원하는 춤을,<br/>
                            모두가 한 화면속에서 즐길 수 있어요.
                        </p>
                        <p>
                            당신의 시간에 맞게 랜플댄 무대가 제공됩니다.
                        </p>
                    </div>
                </div>
                <div className="main-info-icon-wrap">
                    <ul>
                        <li>
                            <Image src={motionIcons} alt=""></Image>
                            <span>춤 모션인식</span>
                        </li>
                        <li>
                            <Image src={personIcon} alt=""></Image>
                            <span>대규모 랜플댄</span>
                        </li>
                        <li>
                            <Image src={shareScreen} alt=""></Image>
                            <span>화면공유</span>
                        </li>
                        <li>
                            <Image src={musicNoteIcon} alt=""></Image>
                            <span>음원싱크</span>
                        </li>
                        <li>
                            <Image src={rankIcon} alt=""></Image>
                            <span>순위산출</span>
                        </li>
                    </ul>
                </div>
            </div>
            <div className="ad-randomplay-wrap">
                <div className="ad-randomplay-info">
                    <div className="ad-randomplay-info-wrap">
                        <h3>
                            언제, 어디서든 랜덤한
                            KPOP이 함께 나오는 곳
                        </h3>
                        <p>
                            언제, 어디서든 장소에 구애받지 않고 원하는 춤을 출 수 있어요
                            당신의 시간에 맞게 랜플댄 무대가 제공됩니다.
                        </p>
                        <span><Link href="/">랜덤플레이댄스 참여하기</Link></span>
                    </div>
                </div>
                <div className="ad-randomplay-img">

                </div>
            </div>
            <div className="realtime-randomplay">
                <div className="realtime-randomplay-title">
                    <h3>실시간 인기 랜플댄</h3>
                    <p>현재 가장 인기가 많은 랜덤플레이댄스방에 참여해보세요.</p>
                </div>
                <div className="realtime-randomplay-content">
                    <ul>
                        <li>
                            <div className="realtime-randomplay-content-img">
                                <Image src={realtimeRandomPlay1} alt=""/>
                            </div>
                            <div className="realtime-randomplay-content-info">
                                <h4>여기서요? 4세대 남돌·여돌 곡 모음</h4>
                                <p>관심 26 | 참여 AM10시 ~ PM3시</p>
                            </div>
                        </li>
                        <li>
                            <div className="realtime-randomplay-content-img">
                                <Image src={realtimeRandomPlay1} alt=""/>
                            </div>
                            <div className="realtime-randomplay-content-info">
                                <h4>여기서요? 4세대 남돌·여돌 곡 모음</h4>
                                <p>관심 26 | 참여 AM10시 ~ PM3시</p>
                            </div>
                        </li>
                        <li>
                            <div className="realtime-randomplay-content-img">
                                <Image src={realtimeRandomPlay1} alt=""/>
                            </div>
                            <div className="realtime-randomplay-content-info">
                                <h4>여기서요? 4세대 남돌·여돌 곡 모음</h4>
                                <p>관심 26 | 참여 AM10시 ~ PM3시</p>
                            </div>
                        </li>
                        <li>
                            <div className="realtime-randomplay-content-img">
                                <Image src={realtimeRandomPlay1} alt=""/>
                            </div>
                            <div className="realtime-randomplay-content-info">
                                <h4>여기서요? 4세대 남돌·여돌 곡 모음</h4>
                                <p>관심 26 | 참여 AM10시 ~ PM3시</p>
                            </div>
                        </li>
                    </ul>
                </div>
            </div>
            <div className="ad-banner-wrap">
                <div className="ad-banner-title-wrap">
                    <h3>SSAFY의 멤버가 되어 <b className="bold">더 많은 랜플댄을 즐겨보세요</b></h3>
                </div>
                <div className="ad-banner-button-wrap">
                    <button><Link href="/">회원가입 후 이용하기</Link></button>
                    <button><Link href="/">랜플댄 방 참여하기</Link></button>
                </div>
            </div>
        </>
    )
}

export default Index;