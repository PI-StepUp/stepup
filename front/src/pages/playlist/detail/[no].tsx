import {useEffect, useState} from "react";
import Header from "components/Header";
import MainBanner from "components/MainBanner";
import Footer from "components/Footer";
import SubNav from "components/subNav";
import { axiosMusic, axiosUser } from "apis/axios";

import Image from "next/image";
import { useRouter } from "next/router";
import Link from "next/link";
import HeartFillIcon from "/public/images/icon-heart-fill.svg";
import HeartEmptyIcon from "/public/images/icon-heart-empty.svg";

import { accessTokenState, refreshTokenState, idState } from "states/states";
import { useRecoilState } from "recoil";

const DetailArticle = () => {
    const router = useRouter();
    const musicId = router.query.no;
    const [article, setArticle] = useState<any>();

    const [accessToken, setAccessToken] = useRecoilState(accessTokenState);
    const [refreshToken, setRefreshToken] = useRecoilState(refreshTokenState);
    const [id, setId] = useRecoilState(idState);

    const addHeart = async () => {
        try{
            await axiosUser.post('/auth', {
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

        if(article.canHeart === 1){
            axiosMusic.post('/apply/heart', {
                musicApplyId: musicId,
            },{
                headers:{
                    Authorization: `Bearer ${accessToken}`,
                }
            }).then(() => {
                alert('좋아요가 추가되었습니다.');
                router.push(`/playlist/detail/${musicId}`);
            })
        }
    }

    const deleteArticle = async () => {

        try{
            await axiosUser.post('/auth', {
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


        await axiosMusic.delete(`/apply/${musicId}`, {
            params:{
                boardId: Number(musicId),
            },
            headers:{
                Authorization: `Bearer ${accessToken}`,
            }
        }).then((data) => {
            console.log(data);
            if(data.data.message === "노래 신청 삭제 완료"){
                alert("해당 게시글이 삭제되었습니다.");
                router.push('/playlist/list');
            }
        })
    }

    useEffect(() => {
        try{
            axiosUser.post('/auth', {
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

        axiosMusic.get(`/apply/detail`,{
            params:{
                musicApplyId: musicId,
            },
            headers:{
                Authorization: `Bearer ${accessToken}`,
            }
        }).then((data) => {
            if(data.data.message === "노래 신청 상세 조회 완료"){
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
                        <button><Link href="/playlist/list">목록보기</Link></button>
                    </div>
                    <div className="detail-main-title">
                        <span>{article?.artist}</span>
                        <h4>{article?.title}</h4>
                        <div className="like-wrap" onClick={addHeart}>
                            {
                                article?.canHeart === 1 ? 
                                <Image src={HeartEmptyIcon} alt=""></Image> :
                                <Image src={HeartFillIcon} alt=""></Image>
                            }
                            
                            <span>{article?.heartCnt}</span>
                        </div>
                    </div>
                    <div className="detail-main-content">
                        <p>{article?.content}</p>
                    </div>
                    <div className="button-wrap">
                        <button onClick={deleteArticle}>삭제하기</button>
                    </div>
                </div>
            </div>
            <Footer></Footer>
        </>
    )
}

export default DetailArticle;