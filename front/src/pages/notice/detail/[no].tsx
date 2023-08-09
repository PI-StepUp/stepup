import {useEffect, useState} from "react";
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

const DetailNotice = () => {
    const router = useRouter();
    const boardId = router.query.no;
    const [article, setArticle] = useState<any>();
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
        try{
            await axiosBoard.delete(`/notice/${boardId}`, {
                params:{
                    boardId: Number(boardId),
                },
                headers:{
                    Authorization: `Bearer ${accessToken}`,
                }
            }).then((data) => {
                console.log(data);
                if(data.data.message === "공지사항 삭제 완료"){
                    alert("해당 게시글이 삭제되었습니다.");
                    router.push('/notice/list');
                }
            })
        }catch(e){
            alert("글 삭제 실패, 관리자에게 문의하세요.");
        }
    }

    useEffect(() => {

        // try{
        //     axiosUser.post('/auth',{
        //         id: id,
        //     },{
        //         headers:{
        //             Authorization: `Bearer ${accessToken}`,
        //             refreshToken: refreshToken,
        //         }
        //     }).then((data) => {
        //         if(data.data.message === "토큰 재발급 완료"){
        //             setAccessToken(data.data.data.accessToken);
        //             setRefreshToken(data.data.data.refreshToken);
        //         }
        //     })
        // }catch(e){
        //     alert('시스템 에러, 관리자에게 문의하세요.');
        // }


        axiosBoard.get(`/notice/${boardId}`).then((data) => {
            if(data.data.message === "공지사항 게시글 조회 완료"){
                setArticle(data.data.data);
            }
        })
    }, [])
    return (
        <>
            <Header></Header>
            <MainBanner></MainBanner>
            <SubNav linkNo="1"></SubNav>
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
                        <button><Link href="/notice/list">목록보기</Link></button>
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
                        <button onClick={() => router.push('/notice/edit/' + article.boardId)}>수정하기</button>
                    </div>
                </div>
            </div>
            <Footer></Footer>
        </>
    )
}

export default DetailNotice;