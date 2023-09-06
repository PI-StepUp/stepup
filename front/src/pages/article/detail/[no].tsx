import {useRef, useEffect, useState} from "react";
import Header from "components/Header";
import MainBanner from "components/MainBanner";
import Footer from "components/Footer";
import SubNav from "components/subNav";
import { axiosBoard, axiosUser } from "apis/axios";

import Image from "next/image";
import { useRouter } from "next/router";
import Link from "next/link";
import CommentDefaultImage from "/public/images/comment-default-img.svg";

import { accessTokenState, refreshTokenState, idState, nicknameState, profileImgState, boardIdState } from "states/states";
import { useRecoilState } from "recoil";

const DetailArticle = () => {
    const router = useRouter();
    const boardId = router.query.no;
    const articleTitle = useRef<any>();
    const articleContent = useRef<any>();
    const commentValue = useRef<any>();

    const [accessToken, setAccessToken] = useRecoilState(accessTokenState);
    const [refreshToken, setRefreshToken] = useRecoilState(refreshTokenState);
    const [id, setId] = useRecoilState(idState);
    const [article, setArticle] = useState<any>();
    const [comments, setComments] = useState<any>();
    const [nickname, setNickname] = useRecoilState(nicknameState);
    const [profileImg, setProfileImg] = useRecoilState(profileImgState);
    const [boardIdStat, setBoardIdStat] = useRecoilState(boardIdState);
    const createdDate = new Date(article?.createdAt);

    const formattedCreatedDate = createdDate.toLocaleString("ko-KR", {
        year: "numeric",
        month: "long",
        day: "numeric",
        hour: "2-digit",
        minute: "2-digit",
      });


    const deleteComment = async (commentId: any) => {
        await axiosBoard.delete(`/comment/${commentId}`, {
            params: {
                commentId: commentId,
            },
            headers:{
                Authorization: `Bearer ${accessToken}`,
            }
        }).then((data) => {
            if(data.data.message === "댓글 삭제 완료"){
                setComments((prevComments: any) =>
                    prevComments.filter((comment: any) => comment.commentId !== commentId)
                );
                commentValue.current.value = "";
                alert("댓글이 삭제되었습니다.");
                router.push(`/article/detail/${boardId}`);
            }
        }).catch((error: any) => {
            if(error.response.data.message === "만료된 토큰"){
                axiosBoard.delete(`/comment/${commentId}`, {
                    params: {
                        commentId: commentId,
                    },
                    headers:{
                        refreshToken: refreshToken,
                    }
                }).then((data) => {
                    if(data.data.message === "토큰 재발급 완료"){
                        setAccessToken(data.data.data.accessToken);
                        setRefreshToken(data.data.data.refreshToken);
                    }
                }).then(() => {
                    axiosBoard.delete(`/comment/${commentId}`, {
                        params: {
                            commentId: commentId,
                        },
                        headers:{
                            Authorization: `Bearer ${accessToken}`,
                        }
                    }).then((data) => {
                        if(data.data.message === "댓글 삭제 완료"){
                            setComments((prevComments: any) =>
                                prevComments.filter((comment: any) => comment.commentId !== commentId)
                            );
                            commentValue.current.value = "";
                            alert("댓글이 삭제되었습니다.");
                            router.push(`/article/detail/${boardId}`);
                        }
                    })
                }).catch((data) => {
                    if(data.response.status === 401){
                        alert("장시간 이용하지 않아 자동 로그아웃 되었습니다.");
                        setNickname("");
                        router.push("/login");
                        return;
                    }

                    if(data.response.status === 500){
                        alert("시스템 에러, 관리자에게 문의하세요.");
                        return;
                    }
                })
            }
        })
    }

    const createComment = async () => {
        await axiosBoard.post(`/comment/${boardIdStat}`, {
            boardId: boardIdStat,
            content: commentValue.current.value,
        },{
            params:{
                boardId: boardIdStat,
            },
            headers:{
                Authorization: `Bearer ${accessToken}`,
            }
        }).then((data) => {
            if(data.data.message === "댓글 등록 완료"){
                let lastCommentId = 1;
                if(comments.length > 0){
                    lastCommentId = comments[comments.length-1].commentId;
                }
                setComments([...comments, {commentId: lastCommentId+1, writerName: nickname, writerProfileImg: profileImg, content: commentValue.current.value}]);
                commentValue.current.value = "";
                alert("댓글이 추가되었습니다.");
                router.reload();
                router.push(`/article/detail/${boardId}`);
            }
        }).catch((error: any) => {
            if(error.response.data.message === "만료된 토큰"){
                axiosBoard.post(`/comment/${boardIdStat}`, {
                    boardId: boardIdStat,
                    content: commentValue.current.value,
                },{
                    params:{
                        boardId: boardIdStat,
                    },
                    headers:{
                        refreshToken: refreshToken,
                    }
                }).then((data) => {
                    if(data.data.message === "토큰 재발급 완료"){
                        setAccessToken(data.data.data.accessToken);
                        setRefreshToken(data.data.data.refreshToken);
                    }
                }).then(() => {
                    axiosBoard.post(`/comment/${boardIdStat}`, {
                        boardId: boardIdStat,
                        content: commentValue.current.value,
                    },{
                        params:{
                            boardId: boardIdStat,
                        },
                        headers:{
                            Authorization: `Bearer ${accessToken}`,
                        }
                    }).then((data) => {
                        if(data.data.message === "댓글 등록 완료"){
                            let lastCommentId = 1;
                            if(comments.length > 0){
                                lastCommentId = comments[comments.length-1].commentId;
                            }
                            setComments([...comments, {commentId: lastCommentId+1, writerName: nickname, writerProfileImg: profileImg, content: commentValue.current.value}]);
                            commentValue.current.value = "";
                            alert("댓글이 추가되었습니다.");
                            router.reload();
                            router.push(`/article/detail/${boardId}`);
                        }
                    })
                }).catch((data) => {
                    if(data.response.status === 401){
                        alert("장시간 이용하지 않아 자동 로그아웃 되었습니다.");
                        setNickname("");
                        router.push("/login");
                        return;
                    }

                    if(data.response.status === 500){
                        alert("시스템 에러, 관리자에게 문의하세요.");
                        return;
                    }
                })
            }
        })
    }

    const deleteArticle = async () => {
        await axiosBoard.delete(`/talk/${boardIdStat}`,{
            params:{
                boardId: boardIdStat,
            },
            headers:{
                Authorization: `Bearer ${accessToken}`,
            }
        }).then((data) => {
            if(data.data.message === "자유게시판 삭제 완료"){
                alert("게시글이 삭제되었습니다.");
                router.push('/article/list');
            }
        }).catch((error: any) => {
            if(error.response.data.message === "만료된 토큰"){
                axiosBoard.delete(`/talk/${boardIdStat}`,{
                    params:{
                        boardId: boardIdStat,
                    },
                    headers:{
                        refreshToken: refreshToken,
                    }
                }).then((data) => {
                    if(data.data.message === "토큰 재발급 완료"){
                        setAccessToken(data.data.data.accessToken);
                        setRefreshToken(data.data.data.refreshToken);
                    }
                }).then(() => {
                    axiosBoard.delete(`/talk/${boardIdStat}`,{
                        params:{
                            boardId: boardIdStat,
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
                }).catch((data) => {
                    if(data.response.status === 401){
                        alert("장시간 이용하지 않아 자동 로그아웃 되었습니다.");
                        setNickname("");
                        router.push("/login");
                        return;
                    }

                    if(data.response.status === 500){
                        alert("시스템 에러, 관리자에게 문의하세요.");
                        return;
                    }
                })
            }
        })
    }

    useEffect(() => {
        axiosBoard.get(`/talk/${boardIdStat}`, {
            params:{
                boardId: boardIdStat,
            },
            headers:{
                Authorization: `Bearer ${accessToken}`,
            }
        }).then((data) => {
            if(data.data.message === "자유게시판 게시글 조회 완료"){
                articleTitle.current.innerText = data.data.data.title;
                articleContent.current.innerText = data.data.data.content;
                setArticle(data.data.data);
                setComments(data.data.data.comments);
            }
        }).catch((error: any) => {
            if(error.response.data.message === "만료된 토큰"){
                axiosBoard.get(`/talk/${boardIdStat}`, {
                    params:{
                        boardId: boardIdStat,
                    },
                    headers:{
                        refreshToken: refreshToken,
                    }
                }).then((data) => {
                    if(data.data.message === "토큰 재발급 완료"){
                        setAccessToken(data.data.data.accessToken);
                        setRefreshToken(data.data.data.refreshToken);
                    }
                }).then(() => {
                    axiosBoard.get(`/talk/${boardIdStat}`, {
                        params:{
                            boardId: boardIdStat,
                        },
                        headers:{
                            Authorization: `Bearer ${accessToken}`,
                        }
                    }).then((data) => {
                        if(data.data.message === "자유게시판 게시글 조회 완료"){
                            articleTitle.current.innerText = data.data.data.title;
                            articleContent.current.innerText = data.data.data.content;
                            setArticle(data.data.data);
                            setComments(data.data.data.comments);
                        }
                    })
                }).catch((data) => {
                    if(data.response.status === 401){
                        alert("장시간 이용하지 않아 자동 로그아웃 되었습니다.");
                        setNickname("");
                        router.push("/login");
                        return;
                    }

                    if(data.response.status === 500){
                        alert("시스템 에러, 관리자에게 문의하세요.");
                        return;
                    }
                })
            }
        })

    }, [])
    return (
        <>
            <Header></Header>
            <MainBanner></MainBanner>
            <SubNav linkNo="2"></SubNav>
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
                        <span>게시글</span>
                        <h4 ref={articleTitle}></h4>
                        <p>2023년 07월 15일 AM 10시</p>
                    </div>
                    <div className="detail-main-content">
                        <p ref={articleContent}></p>
                    </div>
                    <div className="button-wrap">
                        {
                            article?.writerName === nickname ?
                            <>
                                <button onClick={deleteArticle}>삭제하기</button>
                                <button onClick={() => router.push(`/article/edit/${boardId}`)}>수정하기</button>
                            </>
                            :
                            <></>
                        }
                        
                    </div>
                </div>
                <div className="comment-wrap">
                    <ul>
                        {
                            comments?.map((comment, index) => {
                                return(
                                    <li key={index}>
                                        <div className="img-wrap">
                                            <Image src={CommentDefaultImage} alt=""/>
                                        </div>
                                        <div className="comment-main">
                                            <div className="comment-content">
                                                <h5>{comment.writerName}</h5>
                                                <p>{comment.content}</p>
                                            </div>
                                            <div className="comment-button">
                                                {
                                                    comment.writerName === nickname ?
                                                    <button onClick={() => {deleteComment(comment.commentId)}}>댓글삭제</button>
                                                    :
                                                    <></>
                                                }
                                            </div>
                                        </div>
                                    </li>
                                )
                            })
                        }
                    </ul>
                    <div className="comment-input">
                        <textarea name="" id="" ref={commentValue}></textarea>
                        <button onClick={createComment}>댓글등록</button>
                    </div>
                </div>
            </div>
            <Footer></Footer>
        </>
    )
}

export default DetailArticle;