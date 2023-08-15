import {useEffect, useState, useRef} from "react";
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
    const [article, setArticle] = useState<any>();
    const [accessToken, setAccessToken] = useRecoilState(accessTokenState);
    const [refreshToken, setRefreshToken] = useRecoilState(refreshTokenState);
    const [id, setId] = useRecoilState(idState);
    const [nickname, setNickname] = useRecoilState(nicknameState);
    const [profileImg, setProfileImg] = useRecoilState(profileImgState);
    const [boardIdStat, setBoardIdStat] = useRecoilState(boardIdState);
    const [comments, setComments] = useState<any>();
    const commentValue = useRef<any>();

    const createComment = async () => {
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

        await axiosBoard.post(`/comment/${boardId}`, {
            boardId: boardId,
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
                router.push(`/meeting/detail/${boardId}`);
            }
        })
    }

    const deleteComment = async (commentId: any) => {
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
                router.push(`/meeting/detail/${boardId}`);
            }
        })
    }

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
        try{
            await axiosBoard.delete(`/meeting/${boardId}`, {
                params:{
                    boardId: boardIdStat,
                },
                headers:{
                    Authorization: `Bearer ${accessToken}`,
                }
            }).then((data) => {
                if(data.data.message === "정모 삭제 완료"){
                    alert("게시글 삭제 완료");
                    router.push('/meeting/list');
                }
            })
        }catch(e){
            alert("글 삭제 실패, 관리자에게 문의하세요.");
        }
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


        axiosBoard.get(`/meeting/${boardIdStat}`, {
            params:{
                boardId: boardIdStat,
            },
            headers:{
                Authorization: `Bearer ${accessToken}`
            }
        }).then((data) => {
            if(data.data.message === "정모 게시글 조회 완료"){
                setArticle(data.data.data);
                setComments(data.data.data.comments);
            }
        })
    }, [])
    return (
        <>
            <Header></Header>
            <MainBanner></MainBanner>
            <SubNav linkNo="3"></SubNav>
            <div className="detail-article-wrap">
                <div className="detail-title">
                    <span>오프라인 정모</span>
                    <div className="flex-wrap">
                        <h3>글 상세보기</h3>
                        <div className="vertical-line"></div>
                    </div>
                </div>
                <div className="detail-content">
                    <div className="list-wrap">
                        <button><Link href="/meeting/list">목록보기</Link></button>
                    </div>
                    <div className="detail-main-title">
                        <span>오프라인 정모</span>
                        <h4>{article?.title}</h4>
                        <p>2023년 07월 15일 AM 10시</p>
                    </div>
                    <div className="detail-main-content">
                        <p>{article?.content}</p>
                    </div>
                    <div className="button-wrap">
                        {
                            nickname === article?.writerName ?
                            <>
                                <button onClick={deleteArticle}>삭제하기</button>
                                <button onClick={() => router.push('/meeting/edit/' + article.boardId)}>수정하기</button>
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