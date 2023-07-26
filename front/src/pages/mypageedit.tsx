import React, { useState, useEffect } from "react";
import Link from "next/link"
import Header from "../components/Header"
import Footer from "../components/Footer"
import Image from "next/image"
import img_profile from "/public/images/profile-default.png"
import img_leave from "/public/images/icon-leave.svg"

import { useRecoilState } from "recoil";
import { LanguageState } from "states/states";

const MyPageEdit = () => {
	const [lang, setLang] = useRecoilState(LanguageState);
	const [open, setOpen] = useState({ display: 'none' });
	const [option, setOption] = useState<String>(lang === "en" ? "KOREA" : lang === "cn" ? "韩国" : "대한민국");

	// const componentDidMount = () => {
	// 	// 중복 확인 버튼 클릭 이벤트
	// 	const displayWarning = document.getElementById("display-overlapping");
	// 	const clickWarning = (ev: Event) => {
	// 		if (ev.target instanceof HTMLInputElement) {
	// 			displayWarning!.textContent = '중복된 닉네임입니다';
	// 			ev.target;
	// 		}
	// 	}
	// 	const btnElements = document.getElementById("btn-overlapping");
	// 	btnElements?.addEventListener("click", clickWarning);
	// }

	// const label = document.querySelector('.country');
	// const options = document.querySelectorAll('.option-item');
	// // 선택한 옵션을 label에 배치
	// const handleSelect = (item: Element) => {
	// 	const rm = label?.parentNode as Element;
	// 	rm.classList.remove('active');
	// };
	// // 옵션 클릭 시 클릭한 옵션 선택
	// options.forEach(option => {
	// 	option.addEventListener('click', () =>
	// 		handleSelect(option))
	// });
	// 라벨 클릭 시 옵션 목록 열림 닫힘
	// label?.addEventListener('click', () => {
	// 	const status = label.parentNode as Element;
	// 	console.log(status);
	// 	if (status.classList.contains('active'))
	// 		status.classList.remove('active');
	// 	else
	// 		status.classList.add('active');
	// });

	///////////////////////////////////////////////////
	// useEffect(() => { })

	const handleOpen = () => {
		console.log(open.display)
		if (open.display === 'none') {
			setOpen({ display: 'flex' });
		} else {
			setOpen({ display: 'none' })
		}
	}


	// map, 조건부렌더링으로 치환해둘 것!
	const handleSelect = (country: string) => {
		console.log(country);
		switch (country) {
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

	// const changeLang = (country: string) => {
	// 	console.log(country);
	// 	switch (country) {
	// 		case 'USA':
	// 			setOption(lang === "en" ? "USA" : lang === "cn" ? "美国" : "미국");
	// 			break;
	// 		case 'CANADA':
	// 			setOption(lang === "en" ? "CANADA" : lang === "cn" ? "加拿大" : "캐나다");
	// 			break;
	// 		case 'CHINA':
	// 			setOption(lang === "en" ? "CHINA" : lang === "cn" ? "中国" : "중국");
	// 			break;
	// 		case 'JAPAN':
	// 			setOption(lang === "en" ? "JAPAN" : lang === "cn" ? "日本" : "일본");
	// 			break;
	// 		case 'UK':
	// 			setOption(lang === "en" ? "UK" : lang === "cn" ? "英国" : "영국");
	// 			break;
	// 		case 'FRANCE':
	// 			setOption(lang === "en" ? "FRANCE" : lang === "cn" ? "法国" : "프랑스");
	// 			break;
	// 		case 'AUSTRIA':
	// 			setOption(lang === "en" ? "AUSTRIA" : lang === "cn" ? "澳大利亚" : "호주");
	// 			break;
	// 	}
	// }

	///////////////////////////////////////////////////

	return (
		<>
			<Header />
			<div className="background-color">
				<div>
					<ul className="myinfo mb-30">
						<li>
							<div className="list-title mt-70">{lang === "en" ? "PROFILE IMAGE" : lang === "cn" ? "个人资料图片" : "프로필 이미지"}</div>
							<div className="profile">
								<div className="img-box">
									<Image className="img" src={img_profile} alt="profile"></Image>
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
									<input type="text" className="o-input-ol" />
									<p className="o-warning" id="">{lang === "en" ? "Available nickname" : lang === "cn" ? "可用昵称" : "사용 가능한 닉네임입니다"}</p>
								</div>
								<div className="o-btn">
									<input type="button" className="o-btn-ol" id="nickname-overlapping" value={lang === "en" ? "CHECK" : lang === "cn" ? "查看" : "중복 확인"} />
								</div>
							</div>
						</li>
						{/* end - nickname */}
						<li>
							<div className="option mt-25">
								<div className="o-title">{lang === "en" ? "EMAIL" : lang === "cn" ? "电子邮件" : "이메일"}</div>
								<div className="o-input">
									<input type="text" className="o-input-ol" />
									<p className="o-warning" id="">{lang === "en" ? "Available email" : lang === "cn" ? "此电子邮件可用" : "사용 가능한 이메일입니다"}</p>
								</div>
								<div className="o-btn">
									<input type="button" className="o-btn-ol" id="email-overlapping" value={lang === "en" ? "CHECK" : lang === "cn" ? "查看" : "중복 확인"} />
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
											{/* <li className="option-item" onClick={() => handleSelect("USA")} onChange={() => changeLang("USA")}>{lang === "en" ? "USA" : lang === "cn" ? "美国" : "미국"}</li>
											<li className="option-item" onClick={() => handleSelect("CANADA")} onChange={() => changeLang("CANADA")}>{lang === "en" ? "CANADA" : lang === "cn" ? "加拿大" : "캐나다"}</li>
											<li className="option-item" onClick={() => handleSelect("CHINA")} onChange={() => changeLang("CHINA")}>{lang === "en" ? "CHINA" : lang === "cn" ? "中国" : "중국"}</li>
											<li className="option-item" onClick={() => handleSelect("JAPAN")} onChange={() => changeLang("JAPAN")}>{lang === "en" ? "JAPAN" : lang === "cn" ? "日本" : "일본"}</li>
											<li className="option-item" onClick={() => handleSelect("UK")} onChange={() => changeLang("UK")}>{lang === "en" ? "UK" : lang === "cn" ? "英国" : "영국"}</li>
											<li className="option-item" onClick={() => handleSelect("FRANCE")} onChange={() => changeLang("FRANCE")}>{lang === "en" ? "FRANCE" : lang === "cn" ? "法国" : "프랑스"}</li>
										<li className="option-item" onClick={() => handleSelect("AUSTRIA")} onChange={() => changeLang("AUSTRIA")}>{lang === "en" ? "AUSTRIA" : lang === "cn" ? "澳大利亚" : "호주"}</li> */}
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
									<input type="checkbox" id="btn-agreement" />
									<label htmlFor="btn-agreement"></label>
									{/* <input type="checkbox" id="btn-agreement" />
									<label htmlFor="btn-agreement"><span>{lang === "en" ? "I agree to receive emails" : lang === "cn" ? "我同意接收电子邮件" : "이메일 수신에 동의"}</span></label> */}
								</div>
							</div>
						</li>
						{/* end - agreement */}
						<div className="btn-info mt-40">
							<div className="cancel"><Link href="/mypageedit">{lang === "en" ? "CANCEL" : lang === "cn" ? "消除" : "취소"}</Link></div>
							<div className="save"><Link href="/mypageedit">{lang === "en" ? "SAVE" : lang === "cn" ? "节省" : "저장"}</Link></div>
						</div>
					</ul>
				</div>
				{/* end - my information */}
				<div className="myinfo mb-30">
					<div className="list-title mt-70">{lang === "en" ? "CHANGE PASSWORD" : lang === "cn" ? "更改密码" : "비밀번호 변경"}</div>
					<div className="option mt-25">
						<div className="o-pw-title">{lang === "en" ? "New Password" : lang === "cn" ? "新密码" : "새 비밀번호"}</div>
						<div className="o-pw-input">
							<input className="o-pw-input-ol" type="password" />
							<p className="o-warning" id="">{lang === "en" ? "Please include 8-20 letters, numbers, and special characters" : lang === "cn" ? "请包含 8-20 个字母、数字和特殊字符" : "8~20자의 영문, 숫자, 특수문자를 포함해주세요"}</p>
						</div>
					</div>
					<div className="option">
						<div className="o-pw-title">{lang === "en" ? "Confirm New Password" : lang === "cn" ? "确认新密码" : "새 비밀번호 확인"}</div>
						<div className="o-pw-input">
							<input className="o-pw-input-ol" type="password" />
							<p className="o-warning" id="">{lang === "en" ? "Passwords do not match" : lang === "cn" ? "密码不匹配" : "비밀번호가 일치하지 않습니다."}</p>
						</div>
					</div>
					<div className="btn-change mt-40">
						<div className="change"><Link href="/mypageedit">{lang === "en" ? "CHANGE" : lang === "cn" ? "改变" : "변경"}</Link></div>
					</div>
				</div>
				{/* end - change password */}
				<div className="myinfo">
					<div className="list-title mt-70">{lang === "en" ? "DELETE ACCOUNT" : lang === "cn" ? "分裂国家" : "회원 탈퇴"}</div>
					<div className="btn-leave mt-25">
						<Image className="img-leave" src={img_leave}></Image>
						<div className="leave"><Link href="/">{lang === "en" ? "DELETE" : lang === "cn" ? "分裂国家" : "탈퇴"}</Link></div>
						{/* 탈퇴 버튼 클릭시 탈퇴 재질문 모달창 생성 필요 */}
					</div>
				</div>
				{/* end - leave */}
				<div className="background-color mb"></div>
			</div>
			<Footer />
		</>
	)
}

export default MyPageEdit;