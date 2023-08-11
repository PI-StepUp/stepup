import React, { useRef, useState } from "react";
import Header from "components/Header";
import MainBanner from "components/MainBanner";
import SubNav from "components/subNav";
import Footer from "components/Footer";

import { axiosDance, axiosUser } from "apis/axios";
import { useRouter } from "next/router";

import { accessTokenState, refreshTokenState, idState, nicknameState } from "states/states";
import { useRecoilState } from "recoil";

const RoomCreate = () => {
	const roomTitle = useRef<any>();
	const roomContent = useRef<any>();
	const roomStartDate = useRef<any>();
	const roomStartTime = useRef<any>();
	const roomEndTime = useRef<any>();
	const roomMaxNum = useRef<any>();
	const roomFile = useRef<any>();
	const [danceType, setDanceType] = useState('RANKING');

	const [accessToken, setAccessToken] = useRecoilState(accessTokenState);
	const [refreshToken, setRefreshToken] = useRecoilState(refreshTokenState);
	const [id, setId] = useRecoilState(idState);
	const [nickname, setNickname] = useRecoilState(nicknameState);

	const router = useRouter();

	const createRoom = async (e: any) => {
		e.preventDefault();

		try {
			await axiosUser.post('/auth', {
				id: id,
			}, {
				headers: {
					Authorization: `Bearer ${accessToken}`,
					refreshToken: refreshToken,
				}
			}).then((data) => {
				if (data.data.message === "토큰 재발급 완료") {
					setAccessToken(data.data.data.accessToken);
					setRefreshToken(data.data.data.refreshToken);
				}
			})

			await axiosDance.post("", {
				title: roomTitle.current?.value,
				content: roomContent.current?.value,
				startAt: roomStartDate.current?.value + " " + roomStartTime.current?.value,
				endAt: roomStartDate.current?.value + " " + roomEndTime.current?.value,
				danceType: danceType,
				maxUser: Number(roomMaxNum.current?.value),
				thumbnail: "",
				hostId: nickname,
				danceMusicIdList: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10],
			}, {
				headers: {
					Authorization: `Bearer ${accessToken}`,
				}
			}).then((data) => {
				if (data.data.message === "랜덤 플레이 댄스 생성 완료") {
					alert("방 생성이 완료되었습니다.");
					router.push({
						pathname: `/hostroom/${roomTitle.current?.value}`,
						query: {
							hostId: nickname,
							title: roomTitle.current?.value,
							startAt: roomStartTime.current?.value,
							endAt: roomEndTime.current?.value,
							maxUser: Number(roomMaxNum.current?.value),
							token: accessToken,
						}
					});
				}
			})

		} catch (e) {
			console.log(e);
			alert('시스템 에러, 방 생성에 실패하였습니다. 관리자에게 문의하세요.');
		}

    }
    return(
        <>
            <Header/>
            <MainBanner/>
            <div className="create-wrap">
                <div className="create-title">
                    <span>RANDOM PLAY</span>
                    <div className="flex-wrap">
                        <h3>새로운 방 생성</h3>
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