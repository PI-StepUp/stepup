import {useRef, useEffect} from "react";
import Header from "components/Header";
import MainBanner from "components/MainBanner";
import Footer from "components/Footer";
import SubNav from "components/subNav";
import { axiosBoard, axiosUser } from "apis/axios";

import Image from "next/image";
import { useRouter } from "next/router";
import Link from "next/link";
import CommentDefaultImage from "/public/images/comment-default-img.svg";

import { accessTokenState, refreshTokenState, idState } from "states/states";
import { useRecoilState } from "recoil";

const DetailArticle = () => {
    const router = useRouter();
    const boardId = router.query.no;
    const articleTitle = useRef<any>();
    const articleContent = useRef<any>();

    const [accessToken, setAccessToken] = useRecoilState(accessTokenState);
    const [refreshToken, setRefreshToken] = useRecoilState(refreshTokenState);
    const [id, setId] = useRecoilState(idState);

    const deleteArticle = async () => {
        try{
            await axiosUser.post('/auth',{
                id: id,
            },{
                headers:{
                    Authorization: `Bearer ${accessToken}`,
                    refreshToken: refreshToken,
                }
            }).then((data) => {
                if(data.data.message === "토큰 재발급 완료"){
                    setAccessToken(data.data.data.accessToken);
                    setRefreshToken(data.data.data.refreshToken);
                }
            })
        }catch(e){
            alert('시스템 에러, 관리자에게 문의하세요.');
        }

        await axiosBoard.delete(`/talk/${boardId}`,{
            params:{
                boardId: Number(boardId),
            },
            headers:{
                Authorization: `Bearer ${accessToken}`,
            }
        }).then((data) => {
            if(data.data.message === "자유게시판 삭제 완료"){
                alert("게시글이 삭제되었습니다.");
                router.push('/article/list');
            }
        })
    }

    useEffect(() => {
        try{
            axiosUser.post('/auth',{
                id: id,
            },{
                headers:{
                    Authorization: `Bearer ${accessToken}`,
                    refreshToken: refreshToken,
                }
            }).then((data) => {
                if(data.data.message === "토큰 재발급 완료"){
                    setAccessToken(data.data.data.accessToken);
                    setRefreshToken(data.data.data.refreshToken);
                }
            })
        }catch(e){
            alert('시스템 에러, 관리자에게 문의하세요.');
        }

        axiosBoard.get(`/talk/${boardId}`, {
            params:{
                boardId: Number(boardId),
            },
            headers:{
                Authorization: `Bearer ${accessToken}`,
            }
        }).then((data) => {
            if(data.data.message === "자유게시판 게시글 조회 완료"){
                articleTitle.current.innerText = data.data.data.title;
                articleContent.current.innerText = data.data.data.content;
            }
        });
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
                        <h4 ref={articleTitle}></h4>
                        <p>2023년 07월 15일 AM 10시</p>
                    </div>
                    <div className="detail-main-content">
                        <p ref={articleContent}></p>
                    </div>
                    <div className="button-wrap">
                        <button onClick={deleteArticle}>삭제하기</button>
                        <button onClick={() => router.push(`/article/edit/${boardId}`)}>수정하기</button>
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