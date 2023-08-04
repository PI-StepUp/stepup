import {useEffect, useState} from "react";

import Header from "components/Header";
import MainBanner from "components/MainBanner";
import Footer from "components/Footer";
import LanguageButton from "components/LanguageButton";

import Image from "next/image";
import Link from "next/link";
import DefaultProfileImage from "/public/images/playlist-default-profile-img.svg";
import HeartFillIcon from "/public/images/icon-heart-fill.svg";
import HeartEmptyIcon from "/public/images/icon-heart-empty.svg";

import { accessTokenState, refreshTokenState, idState } from "states/states";
import { useRecoilState } from "recoil";
import { LanguageState } from "states/states";

import { axiosMusic, axiosUser } from "apis/axios";

const PlayList = () => {
    const [lang, setLang] = useRecoilState(LanguageState);
    const [playlist, setPlaylist] = useState<any[]>();

    const [accessToken, setAccessToken] = useRecoilState(accessTokenState);
    const [refreshToken, setRefreshToken] = useRecoilState(refreshTokenState);
    const [id, setId] = useRecoilState(idState);

    useEffect(() => {

        axiosMusic.get("/apply",{
            params:{
                keyword:"",
            },
        }).then((data) => {
            console.log(data);
            setPlaylist(data.data.data);
        })
    }, []);
    
    return (
        <>
            <Header/>
            <MainBanner/>
            <div className="playlist-wrap">
                <div className="playlist-title">
                    <span>THE NEW STEPUP’S PLAYLIST</span>
                    <h3>
                        {lang==="en" ? "STEPUP's new playlist" : lang==="cn" ? "STEPUP的新歌单" : "STEPUP의 새로운 플레이리스트" }<br/>
                        {lang==="en" ? "Enjoy it with more songs" : lang==="cn" ? "用更多的歌曲来享受吧" : "더 많은 곡으로 즐기세요" }
                    </h3>
                </div>
                <div className="playlist-create-button-wrap">
                    <button><Link href="/playlist/create">{lang==="en" ? "Request" : lang==="cn" ? "申请新曲" : "신곡 신청하기" }</Link></button>
                </div>
                <div className="playlist-content-wrap">
                    <ul>
                        {playlist?.map((playlist) => {
                            return(
                                <li><Link href={"/playlist/detail/" + playlist.musicApplyId}>
                                    <span>{playlist.artist}</span>
                                    <h4>{playlist.title}</h4>
                                    <p>{playlist.content}</p>
                                    <div className="user-wrap">
                                        <div className="flex-wrap">
                                            {playlist.writerProfileImg === null ? <Image src={DefaultProfileImage} alt=""></Image> : <Image src={playlist.writerProfileImg} alt=""></Image>}
                                            <span>{playlist.writerName}</span>
                                        </div>
                                        <div className="heart-wrap">
                                            {
                                                playlist.canHeart === 1 ?
                                                <Image src={HeartEmptyIcon} alt=""></Image> :
                                                <Image src={HeartFillIcon} alt=""></Image>
                                            }
                                            <span>{playlist.heartCnt}</span>
                                        </div>
                                    </div></Link>
                                </li>
                            )
                        })}
                    </ul>
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
            <LanguageButton/>
            <Footer/>
        </>
    )
}

export default PlayList;