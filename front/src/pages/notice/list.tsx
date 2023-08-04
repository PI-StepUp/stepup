import {useEffect, useState} from  "react";

import Header from "components/Header"
import MainBanner from "components/MainBanner"
import SubNav from "components/subNav"
import Footer from "components/Footer"
import EntranceIcon from "/public/images/entrance-icon.svg"
import Image from "next/image"

import Link from "next/link"

import { useRecoilState } from "recoil";
import { LanguageState } from "states/states";
import { axiosBoard } from "apis/axios";
import Pagination from "react-js-pagination";

const NoticeList = () => {
    const [lang, setLang] = useRecoilState(LanguageState);
    const [notices, setNotices] = useState<any[]>();
    const [page, setPage] = useState<any>(1);

    const handlePageChange = (page: any) => {
        setPage(page);
        console.log(page);
    }

    useEffect(() => {
        axiosBoard.get("/notice",{
            params:{
                keyword: "",
            }
        }).then((data) => {
            console.log(data);
            if(data.data.message === "공지사항 전체 목록 조회 완료"){
                setNotices(data.data.data);
            }
        })
    }, [])
    
    

    return(
        <>
            <Header/>
            <MainBanner/>
            <SubNav linkNo="1"/>
            <div className="notice-list-wrap">
                <ul>
                    {
                        notices?.map((notice, index) => {
                            if(index+1 <= page*10 && index+1 > page*10-10){
                                return(
                                    <li>
                                        <span>{lang==="en" ? "NOTICE" : lang==="cn" ? "公告" : "공지사항" }</span>
                                        <div className="notice-list-content">
                                            <h4>{notice.title}</h4>
                                            <p>{notice.content}</p>
                                        </div>
                                        <div className="flex-wrap">
                                            <div className="entrance-wrap">
                                                <Image src={EntranceIcon} alt=""/>
                                                <span><Link href={"/notice/detail/" + notice.boardId}>{lang==="en" ? "View Details" : lang==="cn" ? "查看细节" : "자세히 보러가기" }</Link></span>
                                            </div>
                                            <div className="edit-button-wrap">
                                                <ul>
                                                    <li><Link href={"/notice/edit/" + notice.boardId}>{lang==="en" ? "MODIFY" : lang==="cn" ? "修改" : "수정하기" }</Link></li>
                                                    <li>{lang==="en" ? "DELETE" : lang==="cn" ? "删除" : "삭제하기" }</li>
                                                </ul>
                                            </div>
                                        </div>
                                    </li>
                                )
                            }
                        }
                    )}
                </ul>
                <div className="notice-create-button">
                    <button><Link href="/notice/create">{lang==="en" ? "CREATE" : lang==="cn" ? "撰写文章" : "글 작성하기" }</Link></button>
                </div>
                <div className="pagination">
                    <ul>
                        <Pagination
                            activePage={page}
                            itemsCountPerPage={10}
                            totalItemsCount={notices?.length}
                            pageRangeDisplayed={9}
                            prevPageText={'<'}
                            nextPageText={'>'}
                            onChange={handlePageChange}
                        />
                    </ul>
                </div>
            </div>
            <Footer/>
        </>
    )
}

export default NoticeList;