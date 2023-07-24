import Header from "../components/Header"
import Footer from "../components/Footer"
import Image from "next/image"
import img_profile from "../public/images/img_profile_default.png"

const componentDidMount = () => {
	// 중복 확인 버튼 클릭 이벤트
	const displayWarning = document.getElementById("display-overlapping");
	const clickWarning = (ev: Event) => {
		if (ev.target instanceof HTMLInputElement) {
			displayWarning!.textContent = '중복된 닉네임입니다';
			ev.target;
		}
	}
	const btnElements = document.getElementById("btn-overlapping");
	btnElements?.addEventListener("click", clickWarning);
}

const MyPageEdit = () => {


	return (
		<>
			<Header />
			<div className="background-color">
				<div>
					<ul className="myinfo">
						<li>
							<div className="list-title mt-70">프로필 이미지</div>
							<div className="profile">
								<div className="img-box">
									<Image className="img" src={img_profile} alt="profile"></Image>
								</div>
								<div className="upload-img">
									<label htmlFor="input-file" className="btn btn-profile">변경</label>
									<input type="file" id="input-file" accept=".jpg, .png" />
									<span className="btn btn-profile btn-profile-remove">삭제</span>
								</div>
								<p className="warning">이미지는 5MB 이내의 jpg, png 파일만 등록 가능합니다</p>
							</div>
						</li>
						{/* end - profile image */}
						<li>
							<div className="option-overlapping">
								<div className="list-title option-overlapping-title mt-40">닉네임</div>
								<input type="text" className="input-overlapping" />
								<input type="button" className="btn overlapping" id="btn-overlapping" value={"중복 확인"} />
							</div>
							<p className="warning" id="display-overlapping">사용 가능한 닉네임입니다</p>
						</li>
						{/* end - nickname */}
						<li>
							<div className="option-overlapping">
								<div className="list-title option-overlapping-title mt-40">이메일</div>
								<input type="email" className="input-overlapping" />
								<input type="button" className="btn overlapping" id="btn-overlapping" value={"중복 확인"} />
							</div>
							<p className="warning" id="display-overlapping">사용 가능한 이메일입니다</p>
						</li>
						{/* end - email */}
						<li>
							<div>

							</div>
						</li>
						{/* end - nation */}
					</ul>
				</div>
				{/* end - my information */}
				<div className="change-pw">

				</div>
				{/* end - change password */}
				<div className="leave">

				</div>
				{/* end - leave */}
			</div>
			<Footer />
		</>
	)
}

export default MyPageEdit;