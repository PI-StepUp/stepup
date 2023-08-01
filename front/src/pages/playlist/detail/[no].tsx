import {useEffect, useState} from "react";
import Header from "components/Header";
import MainBanner from "components/MainBanner";
import Footer from "components/Footer";
import SubNav from "components/subNav";
import { axiosBoard } from "apis/axios";

import Image from "next/image";
import { useRouter } from "next/router";
import Link from "next/link";
import CommentDefaultImage from "/public/images/comment-default-img.svg";

const DetailArticle = () => {
    const router = useRouter();
    const boardId = router.query.no;
    const [article, setArticle] = useState<any>();

    const deleteArticle = async () => {
        await axiosBoard.delete(`/meeting/${boardId}`, {
            params:{
                boardId: Number(boardId),
            }
        }).then((data) => {
            if(data.data.message === "정모 삭제 완료"){
                alert("게시글 삭제 완료");
                router.push('/meeting/list');
            }
        })
    }

    useEffect(() => {
        axiosBoard.get(`/meeting/${boardId}`, {
            params:{
                boardId: boardId,
            }
        }).then((data) => {
            console.log(data);
            if(data.data.message === "정모 게시글 조회 완료"){
                setArticle(data.data.data);
            }
        })
    }, [])
    return (
        <>
            <Header></Header>
            <MainBanner></MainBanner>
            <SubNav></SubNav>
            <div className="detail-article-wrap">
                <div className="detail-title">
                    <span>게시글</span>
                    <div className="flex-wrap">
                        <h3>글 상세보기</h3>
                        <div className="vertical-line"></div>
                    </div>
                </div>
                <div className="detail-content">
                    <div className="list-wrap">
                        <button><Link href="/article/list">목록보기</Link></button>
                    </div>
                    <div className="detail-main-title">
                        <span>공지사항</span>
                        <h4>{article?.title}</h4>
                        <p>2023년 07월 15일 AM 10시</p>
                    </div>
                    <div className="detail-main-content">
                        <p>{article?.content}</p>
                    </div>
                    <div className="button-wrap">
                        <button onClick={deleteArticle}>삭제하기</button>
                        <button>수정하기</button>
                    </div>
                </div>
                <div className="comment-wrap">
                    <ul>
                        <li>
                            <div className="img-wrap">
                                <Image src={CommentDefaultImage} alt=""/>
                            </div>
                            <div className="comment-main">
                                <div className="comment-content">
                                    <h5>Nickname</h5>
                                    <p>댓글의 내용이 들어갈 부분이에요 이곳에 댓글의 내용이</p>
                                </div>
                                <div className="comment-button">
                                    <button>댓글삭제</button>
                                </div>
                            </div>
                        </li>
                        <li>
                            <div className="img-wrap">
                                <Image src={CommentDefaultImage} alt=""/>
                            </div>
                            <div className="comment-main">
                                <div className="comment-content">
                                    <h5>Nickname</h5>
                                    <p>댓글의 내용이 들어갈 부분이에요 이곳에 댓글의 내용이</p>
                                </div>
                                <div className="comment-button">
                                    <button>댓글삭제</button>
                                </div>
                            </div>
                        </li>
                    </ul>
                    <div className="comment-input">
                        <textarea name="" id=""></textarea>
                        <button>댓글등록</button>
                    </div>
                </div>
            </div>
            <Footer></Footer>
        </>
    )
}

export default DetailArticle;