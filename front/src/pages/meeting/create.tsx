import {useRef} from "react";

import Header from "components/Header";
import MainBanner from "components/MainBanner";
import SubNav from "components/subNav";
import Footer from "components/Footer";

import { axiosBoard } from "apis/axios";
import { useRouter } from "next/router";

const MeetingCreate = () => {
    const meetingTitle = useRef();
    const meetingContent = useRef();
    const meetingDate = useRef();
    const meetingStartTime = useRef();
    const meetingEndTime = useRef();
    const meetingRegion = useRef();
    const router = useRouter();
    const createArticle = (e: any) => {
        e.preventDefault();
        axiosBoard.post('/meeting', {
            id: "ssafy",
            title: meetingTitle.current?.value,
            content: meetingContent.current?.value,
            startAt: meetingDate.current?.value + "T" + meetingStartTime.current?.value,
            EndAt: meetingDate.current?.value + "T" + meetingEndTime.current?.value,
            region: meetingRegion.current?.value
        }).then((data) => {
            if(data.data.message === "정모 등록 완료"){
                alert("오프라인 모임 등록이 완료되었습니다.");
                router.push('/meeting/list');
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
                    <form>
                        <table>
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
                        </table>
                    </form>
                </div>
            </div>
            <Footer/>
        </>
    )
}

export default MeetingCreate;