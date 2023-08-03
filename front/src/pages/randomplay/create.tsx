import React, {useRef, useState} from "react";
import Header from "components/Header";
import MainBanner from "components/MainBanner";
import SubNav from "components/subNav";
import Footer from "components/Footer";

import { axiosBoard } from "apis/axios";

const RoomCreate = () => {
    const roomTitle = useRef<any>();
    const roomContent = useRef<any>();
    const roomStartDate = useRef<any>();
    const roomStartTime = useRef<any>();
    const roomEndTime = useRef<any>();
    const roomMaxNum = useRef<any>();
    const roomFile = useRef<any>();
    const [danceType, setDanceType] = useState('랜덤플레이');
    const createRoom = async (e: any) => {
        e.preventDefault();
        const createNotice = await axiosBoard.post("/notice", {
            id: "ssafy",
            title: roomTitle.current?.value,
            content: roomContent.current?.value,
            startAt: roomStartDate.current?.value + "T" + roomStartTime.current?.value,
            endAt: roomStartDate.current?.value + "T" + roomEndTime.current?.value,
            danceType: "",
            maxUser: roomMaxNum.current?.value,
            thumbnail: " ",
            hostId: "ssafy",
            danceMusicIdList: [],
        })
        console.log(createNotice);
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
                                <td>방 제목</td>
                                <td><input type="text" placeholder="제목을 입력해주세요." className="input-title" ref={roomTitle}/></td>
                            </tr>
                            <tr>
                                <td>방 소개</td>
                                <td><textarea className="input-content" placeholder="내용을 입력해주세요." ref={roomContent}></textarea></td>
                            </tr>
                            <tr>
                                <td>방 유형</td>
                                <td>
                                    <select name="" id="">
                                        <option value="">랜덤플레이</option>
                                        <option value="">서바이벌</option>
                                        <option value="">자율모드</option>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td>개최 날짜</td>
                                <td><input type="date" placeholder="시간을 입력해주세요." className="input-date" ref={roomStartDate}/></td>
                            </tr>
                            <tr>
                                <td>개최 시간</td>
                                <td>
                                    <input type="time" placeholder="시간을 입력해주세요." className="input-time" ref={roomStartTime}/> - 
                                    <input type="time" placeholder="시간을 입력해주세요." className="input-time" ref={roomEndTime}/>
                                </td>
                            </tr>
                            <tr>
                                <td>최대 참여자 수</td>
                                <td><input type="number" className="input-max" ref={roomMaxNum}/></td>
                            </tr>
                            <tr>
                                <td>대표이미지</td>
                                <td><input type="file" ref={roomFile}/></td>
                            </tr>
                            <tr>
                                <td>플레이리스트</td>
                                <td></td>
                            </tr>
                            <tr>
                                <td></td>
                                <td>
                                    <div className="create-button-wrap">
                                        <ul>
                                            <li><button>취소하기</button></li>
                                            <li><button onClick={createRoom}>작성하기</button></li>
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

export default RoomCreate;