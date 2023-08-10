import React, { ReactElement, useRef, useState } from 'react';
import { LanguageState } from "states/states";
import { accessTokenState } from "states/states";
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
	const roomStartDate = useRef<any>(props.startAt.split(' ')[0]);
	const roomStartTime = useRef<any>(props.startAt.split(' ')[1]);
	const roomEndTime = useRef<any>(props.endAt);
	const roomMaxNum = useRef<any>(props.maxUser);
	const [roomImg, setRoomImg] = useState<string | ArrayBuffer | null>(props.thumbnail);
	const [danceType, setDanceType] = useState(props.danceType);
	const [lang, setLang] = useRecoilState(LanguageState);
	const [accessToken, setAccessToken] = useRecoilState(accessTokenState);

	// 랜플댄 수정
	const updateRPD = async () => {
		await axiosDance.put("/my", {
			randomDanceId: props.randomDanceId,
			title: roomTitle.current?.value,
			content: roomContent.current?.value,
			startAt: roomStartDate.current?.value + " " + roomStartTime.current?.value,
			endAt: roomStartDate.current?.value + " " + roomEndTime.current?.value,
			danceType: danceType,
			maxUser: Number(roomMaxNum.current?.value),
			thumbnail: roomImg,
			hostId: props.hostId,
			danceMusicIdList: props.danceMusicIdList,
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

					<div className="create-content">
						<form action="">
							<table>
								<tr>
									<td>방 제목</td>
									<td><input type="text" placeholder="제목을 입력해주세요." className="input-title" ref={roomTitle} /></td>
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
									<td><input type="date" placeholder="시간을 입력해주세요." className="input-date" ref={roomStartDate} /></td>
								</tr>
								<tr>
									<td>개최 시간</td>
									<td>
										<input type="time" placeholder="시간을 입력해주세요." className="input-time" ref={roomStartTime} /> -
										<input type="time" placeholder="시간을 입력해주세요." className="input-time" ref={roomEndTime} />
									</td>
								</tr>
								<tr>
									<td>최대 참여자 수</td>
									<td><input type="number" className="input-max" ref={roomMaxNum} /></td>
								</tr>
								<tr>
									<td>대표이미지</td>
									<Image className="img" src={roomImg?.toString()} alt="thumbnail" width={100} height={100}></Image>
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