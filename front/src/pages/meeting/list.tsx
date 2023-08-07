import {useEffect, useState} from "react";

import Header from "components/Header";
import MainBanner from "components/MainBanner";
import SubNav from "components/subNav";
import Footer from "components/Footer";
import LanguageButton from "components/LanguageButton";

import defaultMeetingProfileImg from "/public/images/default-meeting-profile.svg"

import { accessTokenState, refreshTokenState, idState } from "states/states";
import { useRecoilState } from "recoil";
import { LanguageState } from "states/states";
import { axiosBoard, axiosUser } from "apis/axios";

import Image from "next/image"
import Link from "next/link"
import { useRouter } from "next/router"
import Pagination from "react-js-pagination"
import { useInView } from "react-intersection-observer";

const MeetingList = () => {
    const [lang, setLang] = useRecoilState(LanguageState);
    const [meetings, setMeetings] = useState<any[]>();
    const [page, setPage] = useState<any>(1);

    const [accessToken, setAccessToken] = useRecoilState(accessTokenState);
    const [refreshToken, setRefreshToken] = useRecoilState(refreshTokenState);
    const [id, setId] = useRecoilState(idState);
    const [meetingTitle, inView] = useInView();

    const handlePageChange = (page: any) => {
        setPage(page);
        console.log(page);
    }

    useEffect(() => {
        axiosBoard.get('/meeting', {
            params: {
                keyword: "",
            }
        }).then((data) => {
            if(data.data.message === "정모 목록 조회 완료"){
                setMeetings(data.data.data);
            }
        })
    }, [inView])
    return (
        <>
            <Header/>
            <MainBanner/>
            <SubNav linkNo="3"/>
            <div className="meeting-wrap">
                <div className="block-center-wrap">
                    {
                        inView ?
                        <div className="geography-wrap" >
                            <div className="geography-title" ref={meetingTitle}>
                                <h3 style={{animationName: "slide-up-animation"}}>
                                    View the World<br/>
                                    Random Play Dance
                                </h3>
                                <span style={{animationName: "slide-down-animation"}}>KR : 한국</span>
                            </div>
                        </div>
                        :
                        <div className="geography-wrap">
                            <div className="geography-title" ref={meetingTitle}>
                                <h3>
                                    View the World<br/>
                                    Random Play Dance
                                </h3>
                                <span>KR : 한국</span>
                            </div>
                        </div>
                    }
                    <div className="button-wrap">
                        <button><Link href="/meeting/create">{lang==="en" ? "CREATE" : lang==="cn" ? "撰写文章" : "글 작성하기" }</Link></button>
                    </div>
                    <div className="meeting-content-wrap">
                        <ul>
                            {
                                meetings?.map((meeting, index) => {
                                    if(index+1 <= page*10 && index+1 > page*10-10){
                                        return(
                                            <li>
                                                <div className="user-wrap">
                                                    {meeting.profileImg === null ? <Image src={defaultMeetingProfileImg} alt=""/> : <Image src={meeting.profileImg} alt=""/>}
                                                    <p>{meeting.writerName}</p>
                                                </div>
                                                <div className="meeting-content">
                                                    <p>{meeting.content}</p>
                                                    <span><Link href={"/meeting/detail/" + meeting.boardId}>{lang==="en" ? "Offer someone to go with you" : lang==="cn" ? "提议一起去的人" : "같이 갈 사람 제의하기" }</Link></span>
                                                </div>
                                            </li>
                                        )
                                    }
                                })
                            }
                        </ul>
                    </div>
                    <div className="pagination">
                        <ul>
                            <Pagination
                                activePage={page}
                                itemsCountPerPage={10}
                                totalItemsCount={meetings?.length}
                                pageRangeDisplayed={9}
                                prevPageText={'<'}
                                nextPageText={'>'}
                                onChange={handlePageChange}
                            />
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