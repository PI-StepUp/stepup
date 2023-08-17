import {useRef} from "react";

import Header from "components/Header";
import MainBanner from "components/MainBanner";
import SubNav from "components/subNav";
import Footer from "components/Footer";

import { axiosBoard, axiosUser } from "apis/axios";
import { useRouter } from "next/router";

import { useRecoilState } from "recoil";
import { accessTokenState, refreshTokenState, idState, nicknameState } from "states/states";

const MeetingCreate = () => {
    const meetingTitle = useRef<any>();
    const meetingContent = useRef<any>();
    const meetingDate = useRef<any>();
    const meetingStartTime = useRef<any>();
    const meetingEndTime = useRef<any>();
    const meetingRegion = useRef<any>();

    const [accessToken, setAccessToken] = useRecoilState(accessTokenState);
    const [refreshToken, setRefreshToken] = useRecoilState(refreshTokenState);
    const [id, setId] = useRecoilState(idState);
    const [nickname, setNickname] = useRecoilState(nicknameState);
    const router = useRouter();
    const createArticle = async (e: any) => {
        e.preventDefault();

        await axiosBoard.post('/meeting', {
            title: meetingTitle.current?.value,
            content: meetingContent.current?.value,
            startAt: meetingDate.current?.value + "T" + meetingStartTime.current?.value,
            endAt: meetingDate.current?.value + "T" + meetingEndTime.current?.value,
            region: meetingRegion.current?.value,
        }, {
            headers:{
                Authorization: `Bearer ${accessToken}`,
            }
        }).then((data) => {
            if(data.data.message === "정모 등록 완료"){
                alert("오프라인 모임 등록이 완료되었습니다.");
                router.push('/meeting/list');
            }
        }).catch((error: any) => {
            if(error.response.data.message === "만료된 토큰"){
                axiosBoard.post('/meeting', {
                    title: meetingTitle.current?.value,
                    content: meetingContent.current?.value,
                    startAt: meetingDate.current?.value + "T" + meetingStartTime.current?.value,
                    endAt: meetingDate.current?.value + "T" + meetingEndTime.current?.value,
                    region: meetingRegion.current?.value,
                }, {
                    headers:{
                        refreshToken: refreshToken,
                    }
                }).then((data) => {
                    if(data.data.message === "토큰 재발급 완료"){
                        setAccessToken(data.data.data.accessToken);
                        setRefreshToken(data.data.data.refreshToken);
                    }
                }).then(() => {
                    axiosBoard.post('/meeting', {
                        title: meetingTitle.current?.value,
                        content: meetingContent.current?.value,
                        startAt: meetingDate.current?.value + "T" + meetingStartTime.current?.value,
                        endAt: meetingDate.current?.value + "T" + meetingEndTime.current?.value,
                        region: meetingRegion.current?.value,
                    }, {
                        headers:{
                            Authorization: `Bearer ${accessToken}`,
                        }
                    }).then((data) => {
                        if(data.data.message === "정모 등록 완료"){
                            alert("오프라인 모임 등록이 완료되었습니다.");
                            router.push('/meeting/list');
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
    return(
        <>
            <Header/>
            <MainBanner/>
            <SubNav linkNo="3"/>
            <div className="create-wrap">
                <div className="create-title">
                    <span>게시글</span>
                    <div className="flex-wrap">
                        <h3>글 작성</h3>
                        <div className="horizontal-line"></div>
                    </div>
                </div>
                <div className="create-content">
                    <form>
                        <table>
                            <tbody>
                                <tr>
                                    <td>제목</td>
                                    <td><input type="text" placeholder="제목을 입력해주세요." className="input-title" ref={meetingTitle}/></td>
                                </tr>
                                <tr>
                                    <td>내용</td>
                                    <td><textarea className="input-content" placeholder="내용을 입력해주세요." ref={meetingContent}></textarea></td>
                                </tr>
                                <tr>
                                    <td>모임 날짜</td>
                                    <td><input type="date" placeholder="시간을 입력해주세요." className="input-date" ref={meetingDate}/></td>
                                </tr>
                                <tr>
                                    <td>모임 시간</td>
                                    <td>
                                        <input type="time" placeholder="시간을 입력해주세요." className="input-time" ref={meetingStartTime}/>- 
                                        <input type="time" placeholder="시간을 입력해주세요." className="input-time" ref={meetingEndTime}/>
                                    </td>
                                </tr>
                                <tr>
                                    <td>지역</td>
                                    <td><input type="text" placeholder="지역을 입력해주세요." className="input-region" ref={meetingRegion}/></td>
                                </tr>
                                <tr>
                                    <td></td>
                                    <td>
                                        <div className="create-button-wrap">
                                            <ul>
                                                <li><button>취소하기</button></li>
                                                <li><button onClick={createArticle}>작성하기</button></li>
                                            </ul>
                                        </div>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </form>
                </div>
            </div>
            <Footer/>
        </>
    )
}

export default MeetingCreate;