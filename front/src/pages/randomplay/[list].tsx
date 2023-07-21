import Header from "components/Header"
import MainBanner from "components/MainBanner"
import Footer from "components/Footer"

import Image from "next/image"
import ArticleIcon from "/public/images/article-icon.svg"
import RandomplayThumbnail from "/public/images/randomplay-thumbnail.png"

const RandomPlayList = () => {
    return(
        <>
            <Header/>
            <MainBanner/>
            <div className="randomplay-list-wrap">
                <section>
                    <div className="section-title">
                        <div className="flex-wrap">
                            <Image src={ArticleIcon} alt=""/>
                            <h3>현재 인기 있는 랜덤 플레이 댄스</h3>
                        </div>
                         <button>개최하기</button>
                    </div>
                    <div className="section-content">
                        <ul>
                            <li>
                                <div className="section-content-img">
                                    <span>서바이벌</span>
                                    <Image src={RandomplayThumbnail} alt=""/>
                                </div>
                                <div className="section-content-info">
                                    <h4>여기서요? 4세대 남돌·여돌 곡 모음 여기서요? 4세대 남돌·여돌 곡 모음 여기서요? 4세대 남돌·여돌 곡 모음</h4>
                                    <span>호스트이름</span>
                                    <div className="flex-wrap">
                                        <button>예약하기</button>
                                        <span>참여 PM6시 ~ PM7시</span>
                                    </div>
                                </div>
                            </li>
                            <li>
                                <div className="section-content-img">
                                    <span>서바이벌</span>
                                    <Image src={RandomplayThumbnail} alt=""/>
                                </div>
                                <div className="section-content-info">
                                    <h4>여기서요? 4세대 남돌·여돌 곡 모음</h4>
                                    <span>호스트이름</span>
                                    <div className="flex-wrap">
                                        <button>예약하기</button>
                                        <span>참여 PM6시 ~ PM7시</span>
                                    </div>
                                </div>
                            </li>
                            <li>
                                <div className="section-content-img">
                                    <span>서바이벌</span>
                                    <Image src={RandomplayThumbnail} alt=""/>
                                </div>
                                <div className="section-content-info">
                                    <h4>여기서요? 4세대 남돌·여돌 곡 모음</h4>
                                    <span>호스트이름</span>
                                    <div className="flex-wrap">
                                        <button>예약하기</button>
                                        <span>참여 PM6시 ~ PM7시</span>
                                    </div>
                                </div>
                            </li>
                        </ul>
                    </div>
                    <div className="more-button-wrap">
                        <button>더보기</button>
                    </div>
                </section>
                <section>
                    <div className="section-title">
                        <div className="flex-wrap">
                            <Image src={ArticleIcon} alt=""/>
                            <h3>현재 진행 중인 랜덤 플레이 댄스</h3>
                        </div>
                    </div>
                    <div className="section-content">
                        <ul>
                            <li>
                                <div className="section-content-img">
                                    <span>서바이벌</span>
                                    <Image src={RandomplayThumbnail} alt=""/>
                                </div>
                                <div className="section-content-info">
                                    <h4>여기서요? 4세대 남돌·여돌 곡 모음 여기서요? 4세대 남돌·여돌 곡 모음 여기서요? 4세대 남돌·여돌 곡 모음</h4>
                                    <span>호스트이름</span>
                                    <div className="flex-wrap">
                                        <button>예약하기</button>
                                        <span>참여 PM6시 ~ PM7시</span>
                                    </div>
                                </div>
                            </li>
                            <li>
                                <div className="section-content-img">
                                    <span>서바이벌</span>
                                    <Image src={RandomplayThumbnail} alt=""/>
                                </div>
                                <div className="section-content-info">
                                    <h4>여기서요? 4세대 남돌·여돌 곡 모음</h4>
                                    <span>호스트이름</span>
                                    <div className="flex-wrap">
                                        <button>예약하기</button>
                                        <span>참여 PM6시 ~ PM7시</span>
                                    </div>
                                </div>
                            </li>
                            <li>
                                <div className="section-content-img">
                                    <span>서바이벌</span>
                                    <Image src={RandomplayThumbnail} alt=""/>
                                </div>
                                <div className="section-content-info">
                                    <h4>여기서요? 4세대 남돌·여돌 곡 모음</h4>
                                    <span>호스트이름</span>
                                    <div className="flex-wrap">
                                        <button>예약하기</button>
                                        <span>참여 PM6시 ~ PM7시</span>
                                    </div>
                                </div>
                            </li>
                        </ul>
                    </div>
                    <div className="more-button-wrap">
                        <button>더보기</button>
                    </div>
                </section>
                <section>
                    <div className="section-title">
                        <div className="flex-wrap">
                            <Image src={ArticleIcon} alt=""/>
                            <h3>진행 예정된 랜덤 플레이 댄스</h3>
                        </div>
                    </div>
                    <div className="section-content">
                        <ul>
                            <li>
                                <div className="section-content-img">
                                    <span>서바이벌</span>
                                    <Image src={RandomplayThumbnail} alt=""/>
                                </div>
                                <div className="section-content-info">
                                    <h4>여기서요? 4세대 남돌·여돌 곡 모음 여기서요? 4세대 남돌·여돌 곡 모음 여기서요? 4세대 남돌·여돌 곡 모음</h4>
                                    <span>호스트이름</span>
                                    <div className="flex-wrap">
                                        <button>예약하기</button>
                                        <span>참여 PM6시 ~ PM7시</span>
                                    </div>
                                </div>
                            </li>
                            <li>
                                <div className="section-content-img">
                                    <span>서바이벌</span>
                                    <Image src={RandomplayThumbnail} alt=""/>
                                </div>
                                <div className="section-content-info">
                                    <h4>여기서요? 4세대 남돌·여돌 곡 모음</h4>
                                    <span>호스트이름</span>
                                    <div className="flex-wrap">
                                        <button>예약하기</button>
                                        <span>참여 PM6시 ~ PM7시</span>
                                    </div>
                                </div>
                            </li>
                            <li>
                                <div className="section-content-img">
                                    <span>서바이벌</span>
                                    <Image src={RandomplayThumbnail} alt=""/>
                                </div>
                                <div className="section-content-info">
                                    <h4>여기서요? 4세대 남돌·여돌 곡 모음</h4>
                                    <span>호스트이름</span>
                                    <div className="flex-wrap">
                                        <button>예약하기</button>
                                        <span>참여 PM6시 ~ PM7시</span>
                                    </div>
                                </div>
                            </li>
                        </ul>
                    </div>
                    <div className="more-button-wrap">
                        <button>더보기</button>
                    </div>
                </section>
            </div>
            <Footer/>
        </>
    )
}

export default RandomPlayList