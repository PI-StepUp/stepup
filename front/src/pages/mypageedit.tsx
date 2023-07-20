import Header from "../components/Header"
import Footer from "../components/Footer"
import Image from "next/image"
import img_profile from "../public/images/img_profile_default.png"

const MyPageEdit = () => {
	return (
		<>
			<Header />
			<div className="background_color">
				<div>
					<ul className="myinfo">
						<li>
							<div className="list_title mt-70">프로필 이미지</div>
							<div className="profile">
								<div className="img_box">
									<Image className="img" src={img_profile} alt="profile"></Image>
								</div>
								<div className="upload_img">
									<label htmlFor="input-file" className="btn_upload">변경</label>
									<input type="file" id="input-file" accept=".jpg, .png" />
									<span className="btn_remove">삭제</span>
								</div>
								<p>이미지는 5MB 이내의 jpg, png 파일만 등록 가능합니다</p>
							</div>
						</li>
						{/* end - profile image */}
						<li>
							<div className="nickname">
								<div className="list_title mt-40">닉네임</div>
								<input type="text" />
							</div>
						</li>
					</ul>
				</div>
				{/* end - my information */}
				<div className="change_pw">

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