import {useRef} from "react";

import Header from "components/Header";
import MainBanner from "components/MainBanner";
import SubNav from "components/subNav";
import Footer from "components/Footer";

import { axiosMusic, axiosUser } from "apis/axios";
import { useRouter } from "next/router";

import { accessTokenState, refreshTokenState, idState } from "states/states";
import { useRecoilState } from "recoil";

const PlayListCreate = () => {
    const playlistTitle = useRef<any>();
    const artist = useRef<any>();
    const playlistContent = useRef<any>();

    const [accessToken, setAccessToken] = useRecoilState(accessTokenState);
    const [refreshToken, setRefreshToken] = useRecoilState(refreshTokenState);
    const [id, setId] = useRecoilState(idState);

    const router = useRouter();

    const createPlaylist = (e: any) => {
        e.preventDefault();

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

        axiosMusic.post('/apply',{
            title: playlistTitle.current?.value,
            artist: artist.current?.value,
            content: playlistContent.current?.value,
        },{
            headers:{
                Authorization: `Bearer ${accessToken}`,
            }
        }).then((data) => {
            if(data.data.message === "노래 신청 완료"){
                alert("노래 신청이 완료되었습니다.");
                router.push('/playlist/list');
            }
        })
    }

    return(
        <>
            <Header/>
            <MainBanner/>
            <SubNav/>
            <div className="create-wrap">
                <div className="create-title">
                    <span>게시글</span>
                    <div className="flex-wrap">
                        <h3>글 작성</h3>
                        <div className="horizontal-line"></div>
                    </div>
                </div>
                <div className="create-content">
                    <form action="">
                        <table>
                            <tr>
                                <td>노래 제목</td>
                                <td><input type="text" placeholder="노래 제목을 입력해주세요." className="input-title" ref={playlistTitle}/></td>
                            </tr>
                            <tr>
                                <td>가수</td>
                                <td><input type="text" placeholder="아티스트를 입력해주세요." className="input-title" ref={artist}/></td>
                            </tr>
                            <tr>
                                <td>신청 사유</td>
                                <td><textarea className="input-content" placeholder="내용을 입력해주세요." ref={playlistContent}></textarea></td>
                            </tr>
                            <tr>
                                <td></td>
                                <td>
                                    <div className="create-button-wrap">
                                        <ul>
                                            <li><button>취소하기</button></li>
                                            <li><button onClick={createPlaylist}>작성하기</button></li>
                                        </ul>
                                    </div>
                                </td>
                            </tr>
                        </table>
                    </form>
                </div>
            </div>
            <Footer/>
        </>
    )
}

export default PlayListCreate;