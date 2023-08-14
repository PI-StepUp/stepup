import { useEffect, useState } from "react"

import Header from "components/Header"
import MainBanner from "components/MainBanner"
import Footer from "components/Footer"
import LanguageButton from "components/LanguageButton"

import Link from "next/link"

import Image from "next/image"
import ArticleIcon from "/public/images/article-icon.svg"
import RandomplayThumbnail from "/public/images/randomplay-thumbnail.png"
import TopIcon from "/public/images/icon-top.svg"
import NowIcon from "/public/images/icon-now.svg"
import SoonIcon from "/public/images/icon-soon.svg"

import { accessTokenState, refreshTokenState, idState, nicknameState } from "states/states";
import { useRecoilState } from "recoil";
import { LanguageState } from "states/states";

import { axiosDance, axiosUser } from "apis/axios"

const RandomPlayList = () => {
	const [lang, setLang] = useRecoilState(LanguageState);
	const [rooms, setRooms] = useState<any[]>();
	const [inprogress, setInprogress] = useState<any[]>();
	const [scheduled, setScheduled] = useState<any[]>();

	const [accessToken, setAccessToken] = useRecoilState(accessTokenState);
	const [refreshToken, setRefreshToken] = useRecoilState(refreshTokenState);
	const [id, setId] = useRecoilState(idState);
	const [nickname, setNickname] = useRecoilState(nicknameState);
	
	const [roomsVisibleItems, setRoomsVisibleItems] = useState(6);
	const [inprogressVisibleItems, setInprogressVisibleItems] = useState(3);
	const [scheduledVisibleItems, setScheduledVisibleItems] = useState(3);
	const currentDate = new Date();
	
	useEffect(() => {
		if (accessToken) {
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
		}
		
		axiosDance.get('', {
			params: {
				progressType: "ALL",
			},
			headers: {
				Authorization: `Bearer ${accessToken}`
			}
		}).then((data) => {
			console.log(data);
			if (data.data.message === "참여 가능한 랜덤 플레이 댄스 목록 조회 완료") {
				setRooms(data.data.data);
			}
		})

		axiosDance.get('', {
			params: {
				progressType: "IN_PROGRESS",
			},
			headers: {
				Authorization: `Bearer ${accessToken}`
			}
		}).then((data) => {
			if (data.data.message === "진행 중인 랜덤 플레이 댄스 목록 조회 완료") {
				setInprogress(data.data.data);
			}
		})

		axiosDance.get('', {
			params: {
				progressType: "SCHEDULED",
			},
			headers: {
				Authorization: `Bearer ${accessToken}`
			}
		}).then((data) => {
			if (data.data.message === "진행 예정된 랜덤 플레이 댄스 목록 조회 완료") {
				setScheduled(data.data.data);
			}
		})
	}, []);

	async function reserveRandomDance(randomDanceId: number) {
		if (!accessToken) {
			alert("해당 서비스는 로그인 후 이용하실 수 있습니다.");
			return;
		}

		try {
			await axiosDance.post(`/reserve/${randomDanceId}`, {}, {
				headers: {
					Authorization: `Bearer ${accessToken}`
				}
			})
				.then((data) => {
					if (data.data.message === "랜덤 플레이 댄스 예약 완료") {
						alert("예약을 완료했습니다.");
					}
				});
			location.reload();
		} catch (e) {
			console.log(e);
		}
	}

	async function cancelReservation(randomDanceId: number) {
		const confirmCancel = confirm("예약을 취소하시겠습니까?");
		if (confirmCancel) {
			try {
				await axiosDance.delete(`/my/reserve/${randomDanceId}`, {
					headers: {
						Authorization: `Bearer ${accessToken}`
					}
				})
					.then((data) => {
						if (data.data.message === "랜덤 플레이 댄스 예약 완료") {
							alert("예약을 완료했습니다.");
						}
					});
				location.reload();
			} catch (e) {
				console.log(e);
			}
		}
	}

	function month(date: any) {
		return ("0" + new Date(date).getMonth()).slice(-2);
	}

	function day(date: any) {
		return ("0" + new Date(date).getDay()).slice(-2);
	}

	function hour(date: any) {
		return ("0" + new Date(date).getHours()).slice(-2);
	}

	function minute(date: any) {
		return ("0" + new Date(date).getMinutes()).slice(-2);
	}

	return (
		<>
			<Header />
			<MainBanner />
			<div className="randomplay-list-wrap">
				<section id="popular">
					<div className="section-title">
						<div className="flex-wrap">
							<Image src={ArticleIcon} alt="" />
							<h3>{lang === "en" ? "Popular right now" : lang === "cn" ? "现在很有人气的随机舞蹈" : "현재 인기 있는 랜덤 플레이 댄스"}</h3>
						</div>
						<button><Link href="/randomplay/create">{lang === "en" ? "Holding" : lang === "cn" ? "打开" : "개최하기"}</Link></button>
					</div>
					<div className="section-content">
						<ul>
							{rooms?.slice(0, roomsVisibleItems)?.map((room, index) => {
								return (
									<li key={index}>
											<div className="section-content-img">
											<span>{(room.danceType === "SURVIVAL") ?
												(lang === "en" ? "SURVIVAL" : lang === "cn" ? "生存模式" : "서바이벌")
												:
												(room.danceType === "BASIC") ?
													(lang === "en" ? "BASIC" : lang === "cn" ? "默认模式" : "자유모드")
													:
													(lang === "en" ? "RANKING" : lang === "cn" ? "排名模式" : "랭킹모드")}</span>
												<Image src={RandomplayThumbnail} alt="" />
											</div>
											<div className="section-content-info">
												<h4>{room.title}</h4>
												<span>{room.hostNickname}</span>
												<div className="flex-wrap">
													{
														(currentDate < new Date(room.startAt)) ? 
															(room.reserveStatus === 0) ?
															<button onClick={async () => await reserveRandomDance(room.randomDanceId)} className="blue-button">
																{lang === "en" ? "Reserve" : lang === "cn" ? "预订" : "예약하기"}
															</button>
															: 
															<button onClick={async () => await cancelReservation(room.randomDanceId)} className="blue-button">
																{lang === "en" ? "Cancel" : lang === "cn" ? "取消预约" : "예약취소"}
															</button>
														:
														(currentDate < new Date(room.endAt)) ? 
															(accessToken) ?
																(nickname === room.hostNickname) ?
																<Link href={{
																	pathname: `/hostroom/${room.title}`,
																	query: {
																		hostId: nickname,
																		title: room.title,
																		startAt: room.startAt,
																		endAt: room.endAt,
																		maxUser: Number(room.maxUser),
																		token: accessToken,
																	},
																}}>
																	<button className="orange-button">{lang === "en" ? "Join" : lang === "cn" ? "参与" : "참여하기"}</button> 
																</Link>
																:
																<Link href={{
																	pathname: `/danceroom/${room.randomDanceId}`,
																	query: {
																		title: room.title,
																		content: room.content,
																		startAt: room.startAt,
																		endAt: room.endAt,
																		myName: nickname,
																	},
																}}>
																	<button className="orange-button">{lang === "en" ? "Join" : lang === "cn" ? "参与" : "참여하기"}</button> 
																</Link>
																:
																<button onClick={() => alert("해당 서비스는 로그인 후 이용하실 수 있습니다.")} className="orange-button">
																	{lang === "en" ? "Join" : lang === "cn" ? "参与" : "참여하기"}
																</button> 
																:
																<button className="gray-button">{lang === "en" ? "Finished" : lang === "cn" ? "已结束" : "마감"}</button>
													}
												<span>{month(room.startAt)}/{day(room.startAt)} {hour(room.startAt)}:{minute(room.startAt)} ~ {month(room.endAt)}/{day(room.endAt)} {hour(room.endAt)}:{minute(room.endAt)}</span>
												</div>
											</div>
									</li>
								)
							})}
						</ul>
					</div>
					{rooms && roomsVisibleItems < rooms.length && (
						<div className="more-button-wrap">
							<button onClick={() => setRoomsVisibleItems(roomsVisibleItems + 6)}>
								{lang === "en" ? "View More" : lang === "cn" ? "查看更多" : "더보기"}
							</button>
						</div>
					)}
				</section>
				<section id="now">
					<div className="section-title">
						<div className="flex-wrap">
							<Image src={ArticleIcon} alt="" />
							<h3>{lang === "en" ? "Going on right now" : lang === "cn" ? "正在进行的随机舞蹈" : "현재 진행 중인 랜덤 플레이 댄스"}</h3>
						</div>
					</div>
					<div className="section-content">
						<ul>
							{inprogress?.slice(0, inprogressVisibleItems)?.map((inprogress, index) => {
								return (
									<li key={index}>
										
											<div className="section-content-img">
												<span>{(inprogress.danceType === "SURVIVAL") ?
												(lang === "en" ? "SURVIVAL" : lang === "cn" ? "生存模式" : "서바이벌")
												:
												(inprogress.danceType === "BASIC") ?
													(lang === "en" ? "BASIC" : lang === "cn" ? "默认模式" : "자유모드")
													:
													(lang === "en" ? "RANKING" : lang === "cn" ? "排名模式" : "랭킹모드")}</span>
												<Image src={RandomplayThumbnail} alt="" />
											</div>
											<div className="section-content-info">
												<h4>{inprogress.title}</h4>
												<span>{inprogress.hostNickname}</span>
												<div className="flex-wrap">
														{
															(currentDate < new Date(inprogress.startAt)) ? 
															(inprogress.reserveStatus === 0) ?
																<button onClick={async () => await reserveRandomDance(inprogress.randomDanceId)} className="blue-button">
																{lang === "en" ? "Reserve" : lang === "cn" ? "预订" : "예약하기"}
																</button>
																: 
																<button onClick={async () => await cancelReservation(inprogress.randomDanceId)} className="blue-button">
																{lang === "en" ? "Cancel" : lang === "cn" ? "取消预约" : "예약취소"}
																</button>
														
														: 
																	(currentDate < new Date(inprogress.endAt)) ? 
																	(accessToken) ? 
																	<Link href={{
																		pathname: `/danceroom/${inprogress.randomDanceId}`,
																		query: {
																			title: inprogress.title,
																			content: inprogress.content,
																			startAt: inprogress.startAt,
																			endAt: inprogress.endAt,
																			myName: nickname,
																		},
																	}}>
																		<button className="orange-button">{lang === "en" ? "Join" : lang === "cn" ? "参与" : "참여하기"}</button> 
																	</Link>
																	:
																	<button onClick={() => alert("해당 서비스는 로그인 후 이용하실 수 있습니다.")} className="orange-button">
																	{lang === "en" ? "Join" : lang === "cn" ? "参与" : "참여하기"}
																	</button> 
																	:
																	<button className="gray-button">{lang === "en" ? "Finished" : lang === "cn" ? "已结束" : "마감"}</button>
														}
													<span>{month(inprogress.startAt)}/{day(inprogress.startAt)} {hour(inprogress.startAt)}:{minute(inprogress.startAt)} ~ {month(inprogress.endAt)}/{day(inprogress.endAt)} {hour(inprogress.endAt)}:{minute(inprogress.endAt)}</span>
												</div>
											</div>
										
									</li>
								)
							})}
						</ul>
					</div>
					{inprogress && inprogressVisibleItems < inprogress.length && (
						<div className="more-button-wrap">
							<button onClick={() => setInprogressVisibleItems(inprogressVisibleItems + 6)}>
								{lang === "en" ? "View More" : lang === "cn" ? "查看更多" : "더보기"}
							</button>
						</div>
					)}
				</section>
				<section id="soon">
					<div className="section-title">
						<div className="flex-wrap">
							<Image src={ArticleIcon} alt="" />
							<h3>{lang === "en" ? "Dance scheduled for today" : lang === "cn" ? "预定的随机舞蹈" : "진행 예정된 랜덤 플레이 댄스"}</h3>
						</div>
					</div>
					<div className="section-content">
						<ul>
							{
								scheduled?.slice(0, scheduledVisibleItems)?.map((scheduled, index) => {
									return (
										<li key={index}>
												<div className="section-content-img">
													<span>{(scheduled.danceType === "SURVIVAL") ?
												(lang === "en" ? "SURVIVAL" : lang === "cn" ? "生存模式" : "서바이벌")
												:
												(scheduled.danceType === "BASIC") ?
													(lang === "en" ? "BASIC" : lang === "cn" ? "默认模式" : "자유모드")
													:
													(lang === "en" ? "RANKING" : lang === "cn" ? "排名模式" : "랭킹모드")}</span>
													<Image src={RandomplayThumbnail} alt="" />
												</div>
												<div className="section-content-info">
													<h4>{scheduled.title}</h4>
													<span>{scheduled.hostNickname}</span>
													<div className="flex-wrap">
															{
																(currentDate < new Date(scheduled.startAt)) ? 
																	(
																		(scheduled.reserveStatus === 0) ?
																		<button onClick={async () => await reserveRandomDance(scheduled.randomDanceId)} className="blue-button">
																		{lang === "en" ? "Reserve" : lang === "cn" ? "预订" : "예약하기"}
																		</button>
																		: 
																		<button onClick={async () => await cancelReservation(scheduled.randomDanceId)} className="blue-button">
																		{lang === "en" ? "Cancel" : lang === "cn" ? "取消预约" : "예약취소"}
																		</button>
																	)
																: 
																	(
																		(currentDate < new Date(scheduled.endAt)) ? 
																			(
																				(accessToken) ? 
																				<Link href={{
																					pathname: `/danceroom/${scheduled.randomDanceId}`,
																					query: {
																						title: scheduled.title,
																						content: scheduled.content,
																						startAt: scheduled.startAt,
																						endAt: scheduled.endAt,
																						myName: nickname,
																					},
																				}}>
																					<button className="orange-button">{lang === "en" ? "Join" : lang === "cn" ? "参与" : "참여하기"}</button> 
																				</Link>
																				:
																				<button onClick={() => alert("해당 서비스는 로그인 후 이용하실 수 있습니다.")} className="orange-button">
																				{lang === "en" ? "Join" : lang === "cn" ? "参与" : "참여하기"}
																				</button>
																			)
																		:
																		<button className="gray-button">{lang === "en" ? "Finished" : lang === "cn" ? "已结束" : "마감"}</button>
																	)
															}
														<span>{month(scheduled.startAt)}/{day(scheduled.startAt)} {hour(scheduled.startAt)}:{minute(scheduled.startAt)} ~ {month(scheduled.endAt)}/{day(scheduled.endAt)} {hour(scheduled.endAt)}:{minute(scheduled.endAt)}</span>
													</div>
												</div>
										</li>
									)
								})
							}
						</ul>
					</div>
					{scheduled && scheduledVisibleItems < scheduled.length && (
						<div className="more-button-wrap">
							<button onClick={() => setScheduledVisibleItems(scheduledVisibleItems + 6)}>
								{lang === "en" ? "View More" : lang === "cn" ? "查看更多" : "더보기"}
							</button>
						</div>
					)}
				</section>
				<div className="floating-box">
					<ul>
						<li>
							<a href="#popular">
								<Image src={TopIcon} alt="" />
								<span>{lang === "en" ? "Popular" : lang === "cn" ? "人气" : "인기 랜플댄"}</span>
							</a>
						</li>
						<li>
							<a href="#now">
								<Image src={NowIcon} alt="" />
								<span>{lang === "en" ? "In progress" : lang === "cn" ? "进行中" : "진행중인 랜플댄"}</span>
							</a>
						</li>
						<li>
							<a href="#soon">
								<Image src={SoonIcon} alt="" />
								<span>{lang === "en" ? "Scheduled to proceed" : lang === "cn" ? "预定进行" : "진행예정 랜플댄"}</span>
							</a>
						</li>
					</ul>
				</div>
			</div>
			<LanguageButton />
			<Footer />
		</>
	)
}

export default RandomPlayList