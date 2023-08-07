import React, { useState, useRef, useEffect } from "react";
import Link from "next/link"
import Header from "../components/Header"
import Banner from "components/MypageeditBanner";
import Footer from "../components/Footer"
import Image from "next/image"
import img_profile from "/public/images/profile-default.png"
import img_leave from "/public/images/icon-leave.svg"

import { useRecoilState } from "recoil";
import { useRouter } from "next/router";
import { LanguageState } from "states/states";
import { accessTokenState, refreshTokenState, idState } from "states/states";

import { axiosUser } from "apis/axios";

const MyPageEdit = () => {
	const [lang, setLang] = useRecoilState(LanguageState);
	const [accessToken, setAccessToken] = useRecoilState(accessTokenState);
	const [refreshToken, setRefreshToken] = useRecoilState(refreshTokenState);

	const [loginUser, setLoginUser] = useState<any>();
	const [id, setId] = useState('');
	const [password, setPassword] = useState('');
	const [email, setEmail] = useState('');
	let [emailAlert, setEmailAlert] = useState<number>();
	const [countryId, setcountryId] = useState<number>();
	const [countryCode, setcountryCode] = useState('');
	const [nickname, setNickname] = useState('');
	const [birth, setBirth] = useState('');
	const [profileImg, setProfileImg] = useState('');
	const [point, setPoint] = useState<number>();
	const [rankName, setRankName] = useState('');

	const [nicknameFlag, setNicknameFlag] = useState<boolean>(true);
	const [emailFlag, setEmailFlag] = useState<boolean>(true);
	const [pwFlag, setPwFlag] = useState<boolean>(true);

	const [open, setOpen] = useState({ display: 'none' });
	const [option, setOption] = useState<String>(lang === "en" ? "KOREA" : lang === "cn" ? "韩国" : "대한민국");

	const nicknameValue = useRef<any>();
	const emailValue = useRef<any>();
	const pw1Value = useRef<any>();
	const pw2Value = useRef<any>();

	const router = useRouter();

	const handleOpen = () => {
		console.log(open.display);
		setcountryCode(open.display);
		if (open.display === 'none') {
			setOpen({ display: 'flex' });
		} else {
			setOpen({ display: 'none' })
		}
	}

	// 닉네임 중복 체크
	let nicknameCheck;
	// 이메일 중복 체크
	let emailCheck;
	// 이메일 수신 동의 여부 체크
	let agreementCheck;
	// 회원 정보 수정
	let editInfo;
	// 비밀번호 수정
	let editPw;
	// 회원 탈퇴
	let leaveStepup;

	// map, 조건부렌더링으로 치환해둘 것!
	const handleSelect = (country: string) => {
		console.log(country);
		switch (country) {
			case 'KOREA':
				setOption(lang === "en" ? "KOREA" : lang === "cn" ? "大韩民国" : "대한민국");
				break;
			case 'USA':
				setOption(lang === "en" ? "USA" : lang === "cn" ? "美国" : "미국");
				break;
			case 'CANADA':
				setOption(lang === "en" ? "CANADA" : lang === "cn" ? "加拿大" : "캐나다");
				break;
			case 'CHINA':
				setOption(lang === "en" ? "CHINA" : lang === "cn" ? "中国" : "중국");
				break;
			case 'JAPAN':
				setOption(lang === "en" ? "JAPAN" : lang === "cn" ? "日本" : "일본");
				break;
			case 'UK':
				setOption(lang === "en" ? "UK" : lang === "cn" ? "英国" : "영국");
				break;
			case 'FRANCE':
				setOption(lang === "en" ? "FRANCE" : lang === "cn" ? "法国" : "프랑스");
				break;
			case 'AUSTRIA':
				setOption(lang === "en" ? "AUSTRIA" : lang === "cn" ? "澳大利亚" : "호주");
				break;
		}
	}

	useEffect(() => {
		// 접근 권한(로그인 여부) 확인
		try {
			axiosUser.post('/auth', {
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
		} catch (e) {
			alert('시스템 에러, 관리자에게 문의하세요.');
		}

		// 로그인 유저 정보 조회
		axiosUser.get("", {
			headers: {
				Authorization: `Bearer ${accessToken}`
			}
		}).then((data) => {
			console.log("로그인 유저 정보 조회", data);
			if (data.data.message === "회원정보 조회 완료") {
				setLoginUser(data.data.data);
				setProfileImg(data.data.data.profileImg);
				if (profileImg === null) {
					setProfileImg("profileImg_default.png");
				}
				setId(data.data.data.id);
				setPassword(data.data.data.password);
				setEmail(data.data.data.email);
				setEmailAlert(data.data.data.emailAlert);
				setcountryId(data.data.data.countryId);
				setcountryCode(data.data.data.countryCode);
				setNickname(data.data.data.nickName);
				setBirth(data.data.data.birth);
				setProfileImg(data.data.data.profileImg);
				setPoint(data.data.data.point);
				setRankName(data.data.data.rankName);

				console.log("로그인 유저 정보", loginUser);
			}
		})
	}, [])

	useEffect(() => {

		nicknameCheck = async () => {
			await axiosUser.post("/dupnick", {
				nickname: nicknameValue.current.value,
			}).then((data) => {
				if (data.data.message === "닉네임 사용 가능") {
					console.log("닉네임 사용 가능");
					setNicknameFlag(true);
				} else {
					console.log("닉네임 사용 불가능");
					setNicknameFlag(false);
				}
			})
		}

		emailCheck = async () => {
			await axiosUser.post("/dupemail", {
				nickname: emailValue.current.value,
			}).then((data) => {
				if (data.data.message === "이메일 사용 가능") {
					console.log("이메일 사용 가능");
					setEmailFlag(true);
				} else {
					console.log("이메일 사용 불가능");
					setEmailFlag(false);
				}
			})
		}

		agreementCheck = async () => {
			if (emailAlert = 0) {
				setEmailAlert(1);
			} else {
				setEmailAlert(0);
			}
		}

		editInfo = async () => {
			await axiosUser.post("", {
				headers: {
					Authorization: `Bearer ${accessToken}`
				},
				id: id,
				password: password,
				email: email,
				emailAlert: emailAlert,
				countryId: countryId,
				countryCode: countryCode,
				nickname: nickname,
				birth: birth,
				profileImg: profileImg,
				point: point,
				rankName: rankName,
			}).then((data) => {
				if (data.data.message === "회원정보 수정 완료") {
					console.log("회원정보 수정 완료");
					router.push('/');
				} else {
					console.log("회원정보 수정 미완료");
					alert("회원정보 수정에 실패했습니다. 다시 한 번 시도해주세요.");
				}
			})
		}

		editPw = async () => {
			if (pwFlag) {
				await axiosUser.post("", {
					headers: {
						Authorization: `Bearer ${accessToken}`
					},
					id: id,
					password: password,
					email: email,
					emailAlert: emailAlert,
					countryId: countryId,
					countryCode: countryCode,
					nickname: nickname,
					birth: birth,
					profileImg: profileImg,
					point: point,
					rankName: rankName,
				}).then((data) => {
					if (data.data.message === "비밀번호 수정 완료") {
						console.log("비밀번호 수정 완료");
						router.push('/');
					} else {
						console.log("비밀번호 수정 미완료");
						alert("비밀번호 수정에 실패했습니다. 다시 한 번 시도해주세요.");
					}
				})
			} else {
				alert("비밀번호를 재입력 해주세요.");
			}
		}

		leaveStepup = async () => {
			await axiosUser.delete(`?id=${id}`, {
				headers: {
					Authorization: `Bearer ${accessToken}`
				},
			}).then((data) => {
				if(data.data.message === "회원 탈퇴 완료"){
					console.log("탈퇴 완료");
					alert("탈퇴되었습니다. 이용해주셔서 감사합니다.");
					router.push('/');
				} else {
					alert("회원 탈퇴에 실패하셨습니다. 다시 한 번 시도해주세요.");
				}
			})
		}

		const comparePw = async () => {
			if (pw1Value === pw2Value) {
				setPwFlag(true);
			} else {
				setPwFlag(false);
			}
		}
	})

	return (
		<>
			<Header />
			<Banner />
			<div className="background-color">
				<div>
					<ul className="myinfo mb-30">
						<li>
							{/* 프로필 이미지 업로드 기능 추가 필요 */}
							<div className="list-title mt-70">{lang === "en" ? "PROFILE IMAGE" : lang === "cn" ? "个人资料图片" : "프로필 이미지"}</div>
							<div className="profile">
								<div className="img-box">
									<Image className="img" src={profileImg} alt="profile"></Image>
								</div>
								<div className="upload">
									<div>
										<label htmlFor="input-file" className="btn btn-profile">{lang === "en" ? "CHANGE" : lang === "cn" ? "改变" : "변경"}</label>
										<input type="file" id="input-file" accept=".jpg, .png" />
										<span className="btn btn-profile btn-profile-remove">{lang === "en" ? "DELETE" : lang === "cn" ? "删除" : "삭제"}</span>
									</div>
									<p className="warning">{lang === "en" ? "Only jpg and png files within 5 MB can be registered" : lang === "cn" ? "只能注册 5 MB 以内的 jpg 和 png 文件" : "이미지는 5MB 이내의 jpg, png 파일만 등록 가능합니다"}</p>
								</div>
							</div>
						</li>
						{/* end - profile image */}
						<li>
							<div className="option mt-25">
								<div className="o-title">{lang === "en" ? "NICKNAME" : lang === "cn" ? "昵称" : "닉네임"}</div>
								<div className="o-input">
									<input type="text" className="o-input-ol" ref={nicknameValue} onChange={(e) => setNickname(e.target.value)} />
									{nicknameFlag ? (
										<p className="o-warning" id="">{lang === "en" ? "Available Nickname" : lang === "cn" ? "可用昵称" : "사용 가능한 닉네임입니다"}</p>
									) : (
										<p className="o-warning" id="">{lang === "en" ? "Unavailable Nickname" : lang === "cn" ? "不可用昵称" : "사용 불가능한 닉네임입니다"}</p>
									)}
								</div>
								<div className="o-btn">
									<input onClick={nicknameCheck} type="button" className="o-btn-ol" id="nickname-overlapping" value={lang === "en" ? "CHECK" : lang === "cn" ? "查看" : "중복 확인"} />
								</div>
							</div>
						</li>
						{/* end - nickname */}
						<li>
							<div className="option mt-25">
								<div className="o-title">{lang === "en" ? "EMAIL" : lang === "cn" ? "电子邮件" : "이메일"}</div>
								<div className="o-input">
									<input type="text" className="o-input-ol" ref={emailValue} onChange={(e) => setEmail(e.target.value)} />
									{emailFlag ? (
										<p className="o-warning" id="">{lang === "en" ? "Available Email" : lang === "cn" ? "可用电子邮件" : "사용 가능한 이메일입니다"}</p>
									) : (
										<p className="o-warning" id="">{lang === "en" ? "Unavailable Email" : lang === "cn" ? "不可用电子邮件" : "사용 불가능한 이메일입니다"}</p>
									)}
								</div>
								<div className="o-btn">
									<input onClick={emailCheck} type="button" className="o-btn-ol" id="email-overlapping" value={lang === "en" ? "CHECK" : lang === "cn" ? "查看" : "중복 확인"} />
								</div>
							</div>
						</li>
						{/* end - email */}
						<li>
							<div className="option mt-25">
								<div className="o-title">{lang === "en" ? "NATION" : lang === "cn" ? "国家" : "국가"}</div>
								<div className="o-input">
									<div className="selectbox" onClick={handleOpen}>
										<button className="country">{option}</button>
										<ul className="option-list" style={open}>
											<li className="option-item" onClick={() => handleSelect("USA")}>{lang === "en" ? "USA" : lang === "cn" ? "美国" : "미국"}</li>
											<li className="option-item" onClick={() => handleSelect("CANADA")}>{lang === "en" ? "CANADA" : lang === "cn" ? "加拿大" : "캐나다"}</li>
											<li className="option-item" onClick={() => handleSelect("CHINA")}>{lang === "en" ? "CHINA" : lang === "cn" ? "中国" : "중국"}</li>
											<li className="option-item" onClick={() => handleSelect("JAPAN")}>{lang === "en" ? "JAPAN" : lang === "cn" ? "日本" : "일본"}</li>
											<li className="option-item" onClick={() => handleSelect("UK")}>{lang === "en" ? "UK" : lang === "cn" ? "英国" : "영국"}</li>
											<li className="option-item" onClick={() => handleSelect("FRANCE")}>{lang === "en" ? "FRANCE" : lang === "cn" ? "法国" : "프랑스"}</li>
											<li className="option-item" onClick={() => handleSelect("AUSTRIA")}>{lang === "en" ? "AUSTRIA" : lang === "cn" ? "澳大利亚" : "호주"}</li>
										</ul>
										{/* 언어 변경 시, 선택된 옵션의 언어가 변경되지 않는 문제 해결 필요 */}
									</div>
								</div>
								<div className="o-btn"></div>
							</div>
						</li>
						{/* end - nation */}
						<li>
							<div className="agreement">
								<div className="list-title mt-25">{lang === "en" ? "Allowing e-mail receives" : lang === "cn" ? "允许接收电子邮件" : "이메일 수신 동의"}</div>
								<textarea className="email-description" readOnly>
									{lang === "en" ? `[STEP UP] obtains the recipient's prior consent to transmit advertising information in accordance with relevant laws such as the Personal Information Protection Act, etc., and regularly checks whether or not the recipient of the advertising information consents. However, if you do not agree, there may be restrictions on benefits according to the purpose of use, such as introduction and solicitation of products/services, gift giving events, promotional events, etc. There are no other disadvantages related to financial transactions.

- Transmission method
It can be delivered through your cell phone text message (SMS), Email, etc.
- Transmission contents
The marketing information sent is advertising information such as benefit information, various event information, product information, and new service information provided by services operated by KONA I Co., Ltd. (local currency and KONA Card Shop, etc.) to the recipient. will be dispatched. However, other than advertising information, informational content that is obligatory is provided regardless of whether or not you consent to receiving.
- Withdrawal information
Even after consenting to receiving, you can withdraw your consent according to your will, and even if you do not consent to receiving, you can use the basic services provided by the company, but you may not receive marketing information from us.` : lang === "cn" ? `[STEP UP] 根据个人信息保护法等相关法律，事先征得广告信息接收者的同意，并定期检查广告信息的接收者是否同意。但如果您不同意，根据使用目的，可能会受到产品/服务的介绍和招揽、礼品赠送活动、促销活动等优惠的限制。没有与金融交易相关的其他缺点。

- 传输方式
它可以通过您的手机短信（SMS）、电子邮件等方式发送。
- 传输内容
发送的营销信息是KONA I有限公司运营的服务（当地货币和KONA卡商店等）向接收者提供的优惠信息、各种活动信息、产品信息和新服务信息等广告信息。将被派遣。但是，除了广告信息之外，无论您是否同意接收，都必须提供强制性的信息内容。
- 提款信息
即使同意接收后，您也可以根据自己的意愿撤回同意，即使您不同意接收，您也可以使用本公司提供的基本服务，但您可能无法接收我们发送的营销信息。` : `[STEP UP]은 개인정보보호법 등에 관한 법률 등 관계법령에 따라 광고성 정보를 전송하기 위해 수신자의 사전 수신동의를 받고 있으며, 광고 성 정보 수신자의 수신동의여부를 정기적으로 확인합니다. 다만 동의하지 않을 경우, 상품/서비스 소개 및 권유, 사은행사, 판촉행사 등 이용목적에 따른 혜택의 제한이 있을 수 있습니다. 그 밖에 금융 거래와 관련된 불이익은 없습니다.

- 전송방법
고객님의 핸드폰 문자메시지(SMS), Email 등을 통해 전달될 수 있습니다.
- 전송내용
발송되는 마케팅 정보는 수신자에게 ㈜코나아이가(지역화폐 및 코나카드샵 등) 운영하는 서비스에서 제공하는 혜택 정보, 각종 이벤트 정보, 상품 정보, 신규 서비스 안내 등 광고성 정보로 관련 법의 규정을 준수하여 발송됩니다. 단, 광고성 정보 이외 의무적으로 안내되어야 하는 정보성 내용은 수신동의 여부와 무관하게 제공됩니다.
- 철회안내
고객님은 수신 동의 이후에라도 의사에 따라 동의를 철회할 수 있으며, 수신을 동의하지 않아도 회사가 제공하는 기본적인 서비스를 이용할 수 있으나, 당사의 마케팅 정보를 수신하지 못할 수 있습니다.`}
								</textarea>
								{/* 언어 변경 시, 적용이 되지 않는 문제 해결 필요 */}
								<div className="btn-agreement">
									{/* <Image src={img_checkbox}></Image> */}
									<span>{lang === "en" ? "I agree to receive emails" : lang === "cn" ? "我同意接收电子邮件" : "이메일 수신에 동의"}</span>
									<input type="checkbox" id="btn-agreement" onClick={agreementCheck} />
									<label htmlFor="btn-agreement"></label>
									{/* <input type="checkbox" id="btn-agreement" />
									<label htmlFor="btn-agreement"><span>{lang === "en" ? "I agree to receive emails" : lang === "cn" ? "我同意接收电子邮件" : "이메일 수신에 동의"}</span></label> */}
								</div>
							</div>
						</li>
						{/* end - agreement */}
						<div className="btn-info mt-40">
							<div className="cancel"><Link href="/mypageedit">{lang === "en" ? "CANCEL" : lang === "cn" ? "消除" : "취소"}</Link></div>
							<div className="save" onClick={editInfo}>{lang === "en" ? "SAVE" : lang === "cn" ? "节省" : "저장"}</div>
						</div>
					</ul>
				</div>
				{/* end - my information */}
				<div className="myinfo mb-30">
					<div className="list-title mt-70">{lang === "en" ? "CHANGE PASSWORD" : lang === "cn" ? "更改密码" : "비밀번호 변경"}</div>
					<div className="option mt-25">
						<div className="o-pw-title">{lang === "en" ? "New Password" : lang === "cn" ? "新密码" : "새 비밀번호"}</div>
						<div className="o-pw-input">
							<input className="o-pw-input-ol" type="password" ref={pw1Value} />
							<p className="o-warning" id="">{lang === "en" ? "Please include 8-20 letters, numbers, and special characters" : lang === "cn" ? "请包含 8-20 个字母、数字和特殊字符" : "8~20자의 영문, 숫자, 특수문자를 포함해주세요"}</p>
						</div>
					</div>
					<div className="option">
						<div className="o-pw-title">{lang === "en" ? "Confirm New Password" : lang === "cn" ? "确认新密码" : "새 비밀번호 확인"}</div>
						<div className="o-pw-input">
							<input className="o-pw-input-ol" type="password" ref={pw2Value} />
							{pwFlag ? (
								<p className = "o-warning"></p>
							): (
								<p className = "o-warning">{lang === "en" ? "Passwords do not match" : lang === "cn" ? "密码不匹配" : "비밀번호가 일치하지 않습니다."}</p>
							)}
					</div>
				</div>
				<div className="btn-change mt-40">
					<div className="change" onClick={editPw}>{lang === "en" ? "CHANGE" : lang === "cn" ? "改变" : "변경"}</div>
				</div>
			</div>
			{/* end - change password */}
			<div className="myinfo">
				<div className="list-title mt-70">{lang === "en" ? "DELETE ACCOUNT" : lang === "cn" ? "分裂国家" : "회원 탈퇴"}</div>
				<div className="btn-leave mt-25">
					<Image className="img-leave" src={img_leave} alt="leave"></Image>
					<div className="leave" onClick={leaveStepup}>{lang === "en" ? "DELETE" : lang === "cn" ? "分裂国家" : "탈퇴"}</div>
					{/* 탈퇴 버튼 클릭시 탈퇴 재질문 모달창 생성 필요 */}
				</div>
			</div>
			{/* end - leave */}
			<div className="background-color mb"></div>
		</div >
			<Footer />
		</>
	)
}

export default MyPageEdit;