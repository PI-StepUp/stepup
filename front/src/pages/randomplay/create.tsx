import React, {useRef, useState} from "react";
import Header from "components/Header";
import MainBanner from "components/MainBanner";
import SubNav from "components/subNav";
import Footer from "components/Footer";

import { axiosDance } from "apis/axios";
import { useRouter } from "next/router";

const RoomCreate = () => {
    const roomTitle = useRef<any>();
    const roomContent = useRef<any>();
    const roomStartDate = useRef<any>();
    const roomStartTime = useRef<any>();
    const roomEndTime = useRef<any>();
    const roomMaxNum = useRef<any>();
    const roomFile = useRef<any>();
    const [danceType, setDanceType] = useState('RANKING');

    const router = useRouter();

    const createRoom = async (e: any) => {
        e.preventDefault();
        const createNotice = await axiosDance.post("/", {
            title: roomTitle.current?.value,
            content: roomContent.current?.value,
            startAt: roomStartDate.current?.value + " " + roomStartTime.current?.value,
            endAt: roomStartDate.current?.value + " " + roomEndTime.current?.value,
            danceType: danceType,
            maxUser: Number(roomMaxNum.current?.value),
            thumbnail: "",
            hostId: "ssafy",
            danceMusicIdList: [],
        }).then((data) => {
            if(data.data.message = "랜덤 플레이 댄스 생성 완료"){
                alert("방 생성이 완료되었습니다.");
                router.push('/randomplay/list');
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
                                    <select name="" id="" onChange={(e) => setDanceType(e.target.value)}>
                                        <option value="RANKING">랜덤플레이</option>
                                        <option value="SURVIVAL">서바이벌</option>
                                        <option value="BASIC">자율모드</option>
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