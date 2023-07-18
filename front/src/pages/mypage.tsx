import Header from "../components/Header"
import Footer from "../components/Footer"
import Image from "next/image"
import img_profile from "../public/images/img_profile.png"
import img_rpdance from "../public/images/img_rpdance.png"
import img_settings from "../public/images/img_settings.svg"
import img_vector from "../public/images/img_vector.png"

const MyPage = () => {
	return (
		<>
			<Header />
			<ul className="mypage_profile">
				<li>
					<div className="info">
						<div className="cnt">3</div>
						<div className="cnt_title">예약된 랜플댄</div>
						<div className="btn_info">
							<a href="">랜플댄 시간표 보기</a>
						</div>
					</div>
				</li>
				<li>
					<div className="info">
						<div className="img_profile">
							<Image className="img" src={img_profile} alt="profile"></Image>
						</div>
						<div>
							<progress value="985" max="2000"></progress>
							<div className="progress_text">
								<p>다음 랭킹까지 1,015</p>
								<p>985/2,000</p>
							</div>
							<div className="info_user">
								<p className="ranking">골드</p>
								<p className="name">김싸피</p>
							</div>
						</div>
					</div>
				</li>
				<li>
					<div className="info">
						<div className="cnt">3</div>
						<div className="cnt_title">작성한 글 수</div>
						<div className="btn_info">
							<a href="">게시글 확인하기</a>
						</div>
					</div>
				</li>
			</ul>
			{/* end - profile information */}
			<ul className="list">
				<div>내 예약</div>
				<li className="contents">
					<div className="img_rpdance">
						<Image className="img" src={img_rpdance} alt="reserved"></Image>
					</div>
					<div className="description">
						<div className="time">Korea 5:00PM ~ 6:20PM (KST)</div>
						<div className="title">여기서요? 4세대 남돌 곡 모음</div>
						<div className="learner">Learner</div>
					</div>
					<div className="btn_contents">
						<a href="">예약 취소</a>
						<a href="">방 생성 취소</a>
					</div>
				</li>
				{/* end - one reservation*/}
				<li className="contents">
					<div className="img_rpdance">
						<Image className="img" src={img_rpdance} alt="reserved"></Image>
					</div>
					<div className="description">
						<div className="time">Korea 5:00PM ~ 6:20PM (KST)</div>
						<div className="title">여기서요? 4세대 남돌-여돌 곡 모음 제목이 길면 다음줄로. 최대 2줄</div>
						<div className="learner">Learner</div>
					</div>
					<div className="btn_contents">
						<a href="">예약 취소</a>
						<a href="">방 생성 취소</a>
					</div>
				</li>
				<li className="contents">
					<div className="img_rpdance">
						<Image className="img" src={img_rpdance} alt="reserved"></Image>
					</div>
					<div className="description">
						<div className="time">Korea 5:00PM ~ 6:20PM (KST)</div>
						<div className="title">여기서요? 4세대 남돌-여돌 곡 모음 제목이 길면 다음줄로. 최대 2줄</div>
						<div className="learner">Learner</div>
					</div>
					<div className="btn_contents">
						<a href="">예약 취소</a>
						<a href="">방 생성 취소</a>
					</div>
				</li>
			</ul>
			{/* end - my reservation */}
			<div className="settings">
				<details>
					<summary className="folding">
						<Image className="img_setting" src={img_settings} alt="settings"></Image>
						<p className="settings_title">비밀번호 수정 / 계정 관리</p>
						<Image className="img_vector" src={img_vector} alt="vector"></Image>
					</summary>
					<div>
						회원 정보 수정
					</div>
				</details>
			</div>
			{/* end - settings */}
			
			<Footer />
		</>
	)
}

export default MyPage;