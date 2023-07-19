import Header from "../components/Header"
import Footer from "../components/Footer"
import Image from "next/image"
import img_profile from "../public/images/img_profile.png"
import img_rpdance from "../public/images/img_rpdance.png"
import img_notice from "../public/images/img_notice.png"
import img_offline from "../public/images/img_offline.png"
import img_requestsong from "../public/images/img_requestsong.png"
import img_board from "../public/images/img_board.png"

const MyPage = () => {
	return (
		<>
			<Header />
			<body className="background_color">
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
						<div className="img_box">
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
						<div className="img_box">
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
						<div className="img_box">
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
						<summary>
							<div className="img_setting"></div>
							<p className="settings_title">회원 정보 수정</p>
							<div className="img_vector"></div>
						</summary>
						<div className="enter_password">
							<p>현재 비밀번호</p>
							<input className="pw" type="password" />
							<input className="btn_submit" type="submit" />
						</div>
					</details>
				</div>
				{/* end - settings */}
				<div>
					<ul className="list">
						<div>참여 이력</div>
						<li className="contents">
							<div className="img_box">
								<Image className="img" src={img_rpdance} alt="reserved"></Image>
							</div>
							<div className="description">
								<div className="time">Korea 5:00PM ~ 6:20PM (KST)</div>
								<div className="title_past">여기서요? 4세대 남돌 곡 모음</div>
								<div className="learner">Learner</div>
							</div>
							<div className="rank">
								<p>-</p>
							</div>
						</li>
						{/* end - one past random play dance*/}
						<li className="contents">
							<div className="img_box">
								<Image className="img" src={img_rpdance} alt="reserved"></Image>
							</div>
							<div className="description">
								<div className="time">Korea 5:00PM ~ 6:20PM (KST)</div>
								<div className="title_past">여기서요? 4세대 남돌-여돌 곡 모음 제목이 길면 다음줄로. 최대 2줄</div>
								<div className="learner">Learner</div>
							</div>
							<div className="rank">
								<p>1위</p>
							</div>
						</li>
						<li className="contents">
							<div className="img_box">
								<Image className="img" src={img_rpdance} alt="reserved"></Image>
							</div>
							<div className="description">
								<div className="time">Korea 5:00PM ~ 6:20PM (KST)</div>
								<div className="title_past">여기서요? 4세대 남돌-여돌 곡 모음 제목이 길면 다음줄로. 최대 2줄</div>
								<div className="learner">Learner</div>
							</div>
							<div className="rank">
								<p>3위</p>
							</div>
						</li>
						<a href="" className="btn_more">더보기</a>
					</ul>
				</div>
				{/* end - past random play dance */}
				<div>
					<ul className="list">
						<div>작성한 글</div>
						<li className="contents">
							<div className="img_box">
								<Image className="img" src={img_notice} alt="notice"></Image>
							</div>
							<div className="description">
								<div className="time">공지사항</div>
								<div className="title_past">작성한 공지사항 제목</div>
								<div className="learner">2023.07.15</div>
							</div>
							<div className="detail">
								<a href="">글 보기</a>
							</div>
						</li>
						{/* end - one board*/}
						<li className="contents">
							<div className="img_box">
								<Image className="img" src={img_requestsong} alt="song"></Image>
							</div>
							<div className="description">
								<div className="time">플레이리스트</div>
								<div className="title_past">작성한 노래 신청글 제목 / 제목이 길면 다음줄로. 최대 2줄</div>
								<div className="learner">2023.07.15</div>
							</div>
							<div className="detail">
								<a href="">글 보기</a>
							</div>
						</li>
						<li className="contents">
							<div className="img_box">
								<Image className="img" src={img_offline} alt="reserved"></Image>
							</div>
							<div className="description">
								<div className="time">오프라인 정모</div>
								<div className="title_past">작성한 오프라인 정모 게시글 제목 / 제목이 길면 다음줄로. 최대 2줄</div>
								<div className="learner">2023.07.15</div>
							</div>
							<div className="detail">
								<a href="">글 보기</a>
							</div>
						</li>
						<a href="" className="btn_more">더보기</a>
					</ul>
				</div>
			</body>
			<Footer />
		</>
	)
}

export default MyPage;