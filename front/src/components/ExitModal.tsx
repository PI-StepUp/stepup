import React, { ReactElement, useState } from 'react';
import { LanguageState } from "states/states";
import { accessTokenState, refreshTokenState, idState, nicknameState, profileImgState, rankNameState } from "states/states";
import { useRecoilState } from "recoil";
import router from 'next/router';
import axios from "axios";

interface props {
	open: boolean;
	close: (v: boolean) => void;
}

const Modal = (props: props): ReactElement => {
	const [lang, setLang] = useRecoilState(LanguageState);
	const [accessToken, setAccessToken] = useRecoilState(accessTokenState);
	const [refreshToken, setRefreshToken] = useRecoilState(refreshTokenState);
	const [id, setId] = useRecoilState(idState);
	const [nickname, setNickname] = useRecoilState(nicknameState);
	const [profileImg, setProfileImg] = useRecoilState(profileImgState);
	const [rankname, setRankname] = useRecoilState(rankNameState);

	const { open, close } = props;

	// 회원 탈퇴
	const leaveStepup = async () => {
		await axios.delete(`https://stepup-pi.com:8080/api/user?id=${id}`, {
			headers: {
				Authorization: `Bearer ${accessToken}`
			},
		}).then((data) => {
			if (data.data.message === "회원 탈퇴 완료") {
				setAccessToken("");
				setRefreshToken("");
				setNickname("");
				setId("");
				setProfileImg("");
				setRankname("");
				console.log("탈퇴 완료");
				{lang === "en" ? alert("You have been withdrawn. Thank you for using our service.") : lang === "cn" ? alert("您已经退出。感谢您使用我们的服务。") : alert("탈퇴되었습니다. 이용해주셔서 감사합니다.")}
				router.push('/');
			} else {
				{lang === "en" ? alert("Failed to withdraw from membership. Please try again.") : lang === "cn" ? alert("会员退出失败，请再试一次。") : alert("회원 탈퇴에 실패하셨습니다. 다시 한 번 시도해주세요.")}
			}
		})
	}

	return (
		<div className="ce-modal">
			<div className="modal-wrap">
				<div className="modal-bg" onClick={close}></div>
				<div className="modal-container">
					<h2>{lang === "en" ? "Are you sure you want to withdraw?" : lang === "cn" ? "您确定要退出吗？" : "정말 탈퇴하시겠습니까?"}</h2>
					<div className="modal-text">
						<h4>{lang === "en" ? "Once you withdraw, your membership information cannot be restored." : lang === "cn" ? "一旦您注销，将无法恢复您的会员信息。" : "탈퇴하는 경우, 회원 정보를 다시 복구할 수 없습니다."}</h4>
					</div>
					<div className="btn-box">
						<button onClick={close} className="cancel">{lang === "en" ? "Cancel" : lang === "cn" ? "取消" : "취소"}</button>
						<button onClick={leaveStepup} className="leave">{lang === "en" ? "Delete" : lang === "cn" ? "分裂国家" : "탈퇴"}</button>
					</div>
				</div>
			</div>
		</div>
	)
}

export default Modal;