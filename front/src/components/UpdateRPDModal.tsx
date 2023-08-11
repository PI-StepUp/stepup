import React, { ReactElement, useRef, useState } from 'react';
import { LanguageState, accessTokenState, idState } from "states/states";
import { useRecoilState } from "recoil";
import { axiosDance } from "apis/axios";
import Image from "next/image"
import router from 'next/router';

interface props {
	open: boolean;
	close: (v: boolean) => void;
	randomDanceId: number;
	title: string;
	content: string;
	startAt: any;
	endAt: any;
	danceType: string;
	maxUser: number;
	thumbnail: string;
	hostId: string;
	danceMusicIdList: any;
}

const Modal = (props: props): ReactElement => {
	const roomTitle = useRef<any>(props.title);
	const roomContent = useRef<any>(props.content);
	const startDateString = (props.startAt||'').split(' ')[0];
	const startDate = new Date(startDateString);
	const formattedStartDate = startDate.toISOString().split("T")[0];
	const roomStartDate = useRef<any>(formattedStartDate);
	const startTimeString = (props.startAt||'').split(' ')[1];
	const [startH, startM] = (startTimeString||'').split(":");
	const formattedStartTime = `${startH}:${startM}`;
	const roomStartTime = useRef<any>(formattedStartTime);
	const endTimeString = props.endAt;
	const [endH, endM] = endTimeString.split(":");
	const formattedEndTime = `${endH}:${endM}`;
	const roomEndTime = useRef<any>(formattedEndTime);
	const roomMaxNum = useRef<any>(props.maxUser);
	const [roomImg, setRoomImg] = useState<string | ArrayBuffer | null>(props.thumbnail);
	const [danceType, setDanceType] = useState(props.danceType);
	const [lang, setLang] = useRecoilState(LanguageState);
	const [accessToken, setAccessToken] = useRecoilState(accessTokenState);
	const [id, setId] = useRecoilState(idState);
	const { open, close } = props;

	// 랜플댄 수정
	const updateRPD = async () => {
		// console.log(props.randomDanceId);
		// console.log(roomTitle.current?.value);
		// console.log(roomContent.current?.value);
		// console.log(roomStartDate.current?.value + " " + roomStartTime.current?.value);
		// console.log(roomStartDate.current?.value + " " + roomEndTime.current?.value);
		// console.log(danceType);
		// console.log(Number(roomMaxNum.current?.value));
		// console.log(roomImg);
		// console.log(props.hostId);
		// console.log(props.danceMusicIdList);
		// console.log(accessToken)

		// 랜플댄 정보 수정
		await axiosDance.put("/my", {
			randomDanceId: props.randomDanceId,
			title: roomTitle.current?.value,
			content: roomContent.current?.value,
			startAt: roomStartDate.current?.value + " " + roomStartTime.current?.value,
			endAt: roomStartDate.current?.value + " " + roomEndTime.current?.value,
			danceType: danceType,
			maxUser: Number(roomMaxNum.current?.value),
			thumbnail: roomImg,
			hostId: id,
			danceMusicIdList: [1, 2, 3],
		}, {
			headers: {
				Authorization: `Bearer ${accessToken}`
			}
		}).then((data) => {
			if (data.data.message === "랜덤 플레이 댄스 수정 완료") {
				{ lang === "en" ? alert("Random play dance information has been updated.") : lang === "cn" ? alert("随机播放舞蹈信息已更新。") : alert("랜덤 플레이 댄스 정보를 수정했습니다.") }
				router.push('/mypage');
			} else {
				{ lang === "en" ? alert("Modification was not successful. Please try again.") : lang === "cn" ? alert("修改未成功。请再试一次。") : alert("수정을 완료하지 못했습니다. 다시 한 번 시도해주세요.") }
				router.push('/mypage');
			}
		})
	}

	// 랜플댄 사진 변경
	const onChangeImage = async (e: React.ChangeEvent<HTMLInputElement>) => {
		if (e.target.files) {
			const file = e.target.files[0];
			const reader = new FileReader();
			reader.readAsDataURL(file);
			console.log("reader", reader);
			reader.onload = () => {
				setRoomImg(reader.result);
				console.log("url >>", reader.result);
			}
		}
	}

	return (
		<div className="ce-modal">
			<div className="modal-wrap">
				<div className="modal-bg" onClick={close}></div>
				<div className="modal-container-modify">
					<h2>{lang === "en" ? "Update Information" : lang === "cn" ? "信息已更新。" : "정보 수정"}</h2>


					<div className="modify-content">
						<form action="">
							<table>
								<tr>
									<td className="modify-title">방 제목</td>
									<td><input type="text" className="input-title" ref={roomTitle} defaultValue={props.title}/></td>
								</tr>
								<tr>
									<td className="modify-title">방 소개</td>
									<td><textarea className="input-content" placeholder={props.content} ref={roomContent} defaultValue={props.content}></textarea></td>
								</tr>
								<tr>
									<td className="modify-title">방 유형</td>
									<td>
										<select name="" id="" defaultValue={props.danceType} onChange={(e) => setDanceType(e.target.value)}>
											<option value="RANKING">랜덤플레이</option>
											<option value="SURVIVAL">서바이벌</option>
											<option value="BASIC">자율모드</option>
										</select>
									</td>
								</tr>
								<tr>
									<td className="modify-title">개최 날짜</td>
									<td><input type="date" className="input-date" ref={roomStartDate} defaultValue={formattedStartDate}/></td>
								</tr>
								<tr>
									<td className="modify-title">개최 시간</td>
									<td className="modify-time">
										<input type="time" className="input-time" ref={roomStartTime} defaultValue={formattedStartTime}/>
										<p> - </p>
										<input type="time" className="input-time" ref={roomEndTime} defaultValue={formattedEndTime}/>
									</td>
								</tr>
								<tr>
									<td className="modify-title">최대 참여자 수</td>
									<td><input type="number" className="input-max" ref={roomMaxNum} defaultValue={props.maxUser}/></td>
								</tr>
								<tr>
									<td className="modify-title">대표이미지</td>
									<Image className="img" src={String(roomImg?.toString())} alt="thumbnail" width={100} height={100}></Image>
									<td><input type="file" accept="image/jpg, image/png" onChange={onChangeImage} /></td>
								</tr>
							</table>
						</form>
					</div>
					<div className="btn-box">
						<button onClick={close} className="cancel">{lang === "en" ? "Cancel" : lang === "cn" ? "取消" : "취소"}</button>
						<button onClick={updateRPD} className="update">{lang === "en" ? "UPDATE" : lang === "cn" ? "编辑" : "수정"}</button>
					</div>
				</div>
			</div>
		</div>
	)
}

export default Modal;