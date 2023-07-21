import Header from "components/Header"
import MainBanner from "components/MainBanner"
import SubNav from "components/subNav"
import Footer from "components/Footer"
import EntranceIcon from "/public/images/entrance-icon.svg"
import Image from "next/image"

import Link from "next/link"

const NoticeList = () => {
    return(
        <>
            <Header/>
            <MainBanner/>
            <SubNav/>
            <div className="notice-list-wrap">
                <ul>
                    <li>
                        <span>공지사항</span>
                        <div className="notice-list-content">
                            <h4>여기다가는 공지사항의 제목이 들어갈 부분이에요. 많은 넣으면 이렇게 최대 2줄까지 넣을 수 있죠.</h4>
                            <p>23년 7월 스텝업 이벤트 7월 이벤트 #1. 신규 등록 이벤트 (신규) - 3개월 등록 시 + 1개월 무료 - 신규 단과 등록 시 10% 할인 여기는 본문 내용이에요. 이렇게 많이 넣으면 최대 2줄까지 넣을 수 있어요...</p>
                        </div>
                        <div className="flex-wrap">
                            <div className="entrance-wrap">
                                <Image src={EntranceIcon} alt=""/>
                                <span>자세히 보러가기</span>
                            </div>
                            <div className="edit-button-wrap">
                                <ul>
                                    <li>수정하기</li>
                                    <li>삭제하기</li>
                                </ul>
                            </div>
                        </div>
                    </li>
                    <li>
                        <span>공지사항</span>
                        <div className="notice-list-content">
                            <h4>여기다가는 공지사항의 제목이 들어갈 부분이에요. 많은 넣으면 이렇게 최대 2줄까지 넣을 수 있죠.</h4>
                            <p>23년 7월 스텝업 이벤트 7월 이벤트 #1. 신규 등록 이벤트 (신규) - 3개월 등록 시 + 1개월 무료 - 신규 단과 등록 시 10% 할인 여기는 본문 내용이에요. 이렇게 많이 넣으면 최대 2줄까지 넣을 수 있어요...</p>
                        </div>
                        <div className="flex-wrap">
                            <div className="entrance-wrap">
                                <Image src={EntranceIcon} alt=""/>
                                <span>자세히 보러가기</span>
                            </div>
                            <div className="edit-button-wrap">
                                <ul>
                                    <li>수정하기</li>
                                    <li>삭제하기</li>
                                </ul>
                            </div>
                        </div>
                    </li>
                    <li>
                        <span>공지사항</span>
                        <div className="notice-list-content">
                            <h4>여기다가는 공지사항의 제목이 들어갈 부분이에요. 많은 넣으면 이렇게 최대 2줄까지 넣을 수 있죠.</h4>
                            <p>23년 7월 스텝업 이벤트 7월 이벤트 #1. 신규 등록 이벤트 (신규) - 3개월 등록 시 + 1개월 무료 - 신규 단과 등록 시 10% 할인 여기는 본문 내용이에요. 이렇게 많이 넣으면 최대 2줄까지 넣을 수 있어요...</p>
                        </div>
                        <div className="flex-wrap">
                            <div className="entrance-wrap">
                                <Image src={EntranceIcon} alt=""/>
                                <span>자세히 보러가기</span>
                            </div>
                            <div className="edit-button-wrap">
                                <ul>
                                    <li>수정하기</li>
                                    <li>삭제하기</li>
                                </ul>
                            </div>
                        </div>
                    </li>
                    <li>
                        <span>공지사항</span>
                        <div className="notice-list-content">
                            <h4>여기다가는 공지사항의 제목이 들어갈 부분이에요. 많은 넣으면 이렇게 최대 2줄까지 넣을 수 있죠.</h4>
                            <p>23년 7월 스텝업 이벤트 7월 이벤트 #1. 신규 등록 이벤트 (신규) - 3개월 등록 시 + 1개월 무료 - 신규 단과 등록 시 10% 할인 여기는 본문 내용이에요. 이렇게 많이 넣으면 최대 2줄까지 넣을 수 있어요...</p>
                        </div>
                        <div className="flex-wrap">
                            <div className="entrance-wrap">
                                <Image src={EntranceIcon} alt=""/>
                                <span>자세히 보러가기</span>
                            </div>
                            <div className="edit-button-wrap">
                                <ul>
                                    <li>수정하기</li>
                                    <li>삭제하기</li>
                                </ul>
                            </div>
                        </div>
                    </li>
                </ul>
                <div className="notice-create-button">
                    <button><Link href="/notice/create">글 작성하기</Link></button>
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
            <Footer/>
        </>
    )
}

export default NoticeList;