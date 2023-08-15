import React, { useState, useEffect, useRef } from "react";
import io from 'socket.io-client';

import LeftArrowIcon from "/public/images/icon-left-arrow.svg"
import ReflectIcon from "/public/images/icon-reflect.svg"
import CameraIcon from "/public/images/icon-camera.svg"
import MicIcon from "/public/images/icon-mic.svg"
import MoreIcon from "/public/images/icon-more-dot.svg"
import PlayThumbnail from "/public/images/room-playlist-thumbnail.jpg"
import PlayIcon from "/public/images/icon-play.svg"
import ReflectHoverIcon from "/public/images/icon-hover-reflect.svg"
import MicHoverIcon from "/public/images/icon-hover-mic.svg"
import CameraHoverIcon from "/public/images/icon-hover-camera.svg"
import MoreDotHoverIcon from "/public/images/icon-hover-more-dot.svg"

import { useRecoilState } from "recoil";
import { LanguageState } from "states/states";

import Image from "next/image"
import Link from "next/link"
import { useRouter } from "next/router";

import { axiosMusic, axiosUser, axiosDance, axiosRank } from "apis/axios";
import axios from "axios";
import { accessTokenState, refreshTokenState, idState, nicknameState, profileImgState, rankNameState } from "states/states";

const Hostroom = () => {
    const pc_config = {
        iceServers: [
            // {
            //   urls: 'stun:[STUN_IP]:[PORT]',
            //   'credentials': '[YOR CREDENTIALS]',
            //   'username': '[USERNAME]'
            // },
            {
                urls: 'stun:stun.l.google.com:19302',
            },
        ],
    };
    const SOCKET_SERVER_URL = 'https://stepup-pi.com:4002';
    const socketRef = useRef<any>();
    const [accessToken, setAccessToken] = useRecoilState(accessTokenState);
    const [refreshToken, setRefreshToken] = useRecoilState(refreshTokenState);
    const [id, setId] = useRecoilState(idState);
    const [roomName, setRoomName] = useState<any>();

	const [lang, setLang] = useRecoilState(LanguageState);
	const [reflect, setReflect] = useState(false);
	const [mic, setMic] = useState(false);
	const [camera, setCamera] = useState(false);
	const [moredot, setMoredot] = useState(false);
	const [nickname, setNickname] = useRecoilState(nicknameState);
	const [profileImg, setProfileImg] = useRecoilState(profileImgState);
	const [rankname, setRankname] = useRecoilState(rankNameState);
	const [musics, setMusics] = useState<any>();
	const router = useRouter();
	const roomId = router.query.roomId;
	const [roomTitle, setRoomTitle] = useState(router.query.roomName);
	const title = router.query.title;
	const startAll: any = router.query.startAt;
	const startTime = startAll?.split(":")[0];
	const startMinute = startAll?.split(":")[1];
	const endAll: any = router.query.endAt;
	const endTime = endAll?.split(":")[0];
	const endMinute = endAll?.split(":")[1];
	const hostToken = router.query.token;
	const reflectHover = () => {
		setReflect(true);
	}
	const reflectLeave = () => {
		setReflect(false);
	}
	const micHover = () => {
		setMic(true);
	}
	const micLeave = () => {
		setMic(false);
	}
	const cameraHover = () => {
		setCamera(true);
	}
	const cameraLeave = () => {
		setCamera(false);
	}
	const moreDotHover = () => {
		setMoredot(true);
	}
	const moreDotLeave = () => {
		setMoredot(false);
	}

	const playMusic = (musicId: number) => {
		socketRef.current.emit("playMusic", musicId, roomName);
		alert("새로운 곡이 재생됩니다.");
	}

	const finishRandomPlay = () => {
		socketRef.current.emit("finish", roomName);
		axios.post(`https://stepup-pi.com:8080/api/rank/point`, {
			id: id,
			pointPolicyId: 5,
			randomDanceId: roomId,
			count: 1,
		}, {
			headers: {
				Authorization: `Bearer ${accessToken}`
			}
		}).then((data) => {
			if (data.data.message === "포인트 적립 완료") {
				alert("개최 포인트가 적립되었습니다!");
			}
		}).catch((e) => {
			console.log("포인트 지급 에러 발생", e);
			alert("포인트 적립에 오류가 발생했습니다. 관리자에게 문의 바랍니다.");
		})
		alert("랜덤플레이댄스가 종료되었습니다. 이용해주셔서 감사합니다.");
		router.push('/randomplay/list');
	}

	useEffect(() => {
		socketRef.current = io.connect(SOCKET_SERVER_URL);

		axios.get('https://stepup-pi.com:8080/api/music', {
			params: {
				keyword: "",
			},
			headers: {
				Authorization: `Bearer ${hostToken}`,
			}
		}).then((data) => {
			console.log(data);
			if (data.data.message === "노래 목록 조회 완료") {
				setMusics(data.data.data);
			}
		});

		axiosDance.get('', {
			params: {
				progressType: "ALL",
			}
		}).then((data) => {
			console.log(data);
			if (data.data.message === "참여 가능한 랜덤 플레이 댄스 목록 조회 완료") {
				for (let i = 0; i < data.data.data.length; i++) {
					if (data.data.data[i].title === roomTitle) {
						setRoomName(data.data.data[i].randomDanceId);
					}
				}
			}
		});

	}, [])
	return (
		<>
			<div className="practiceroom-wrap">
				<div className="practice-video-wrap">
					<div className="practice-title">
						<div className="pre-icon">
							<Link href="/"><Image src={LeftArrowIcon} alt="" /></Link>
						</div>
						<div className="room-title">
							<h3>{title}</h3>
							<span>진행시간: {startTime}시 {startMinute}분 - {endTime}시 {endMinute}분</span>
						</div>
					</div>

					<div className="video-content">
						<div className="my-video">
							<video src=""></video>
						</div>
						<div className="yours-video">
							<ul>
								<li>
									<video src=""></video>
								</li>
								<li>
									<video src=""></video>
								</li>
								<li>
									<video src=""></video>
								</li>
								<li>
									<video src=""></video>
								</li>
							</ul>
						</div>
						<div className="control-wrap">
							<ul>
								<li onMouseEnter={reflectHover} onMouseLeave={reflectLeave}>
									<button>
										{reflect ? <Image src={ReflectHoverIcon} alt="" /> : <Image src={ReflectIcon} alt="" />}
									</button>
								</li>
								<li onMouseEnter={micHover} onMouseLeave={micLeave}>
									<button>
										{mic ? <Image src={MicHoverIcon} alt="" /> : <Image src={MicIcon} alt="" />}
									</button>
								</li>
								<li onClick={finishRandomPlay}><button className="exit-button">{lang === "en" ? "End Practice" : lang === "cn" ? "结束练习" : "연습 종료하기"}</button></li>
								<li onMouseEnter={cameraHover} onMouseLeave={cameraLeave}>
									<button>
										{camera ? <Image src={CameraHoverIcon} alt="" /> : <Image src={CameraIcon} alt="" />}
									</button>
								</li>
								<li onMouseEnter={moreDotHover} onMouseLeave={moreDotLeave}>
									<button>
										{moredot ? <Image src={MoreDotHoverIcon} alt="" /> : <Image src={MoreIcon} alt="" />}
									</button>
								</li>
							</ul>
						</div>
					</div>

				</div>
				<div className="musiclist">
					<div className="musiclist-title">
						<h3>{lang === "en" ? "Playlist" : lang === "cn" ? "播放列表" : "스플리"}</h3>
						<span>{musics?.length}</span>
					</div>
					<div className="musiclist-content">
						<ul>
							{musics?.map((music: any, index: any) => {
								return (
									<li key={index}>
										<div className="flex-wrap">
											<div className="musiclist-content-thumbnail">
												<Image src={PlayThumbnail} alt="" />
											</div>
											<div className="musiclist-content-info">
												<h4>{music.title}</h4>
												<span>{music.artist}</span>
											</div>
										</div>
										<div className="musiclist-content-control-icon">
											<span onClick={() => playMusic(music.musicId)}><Image src={PlayIcon} alt="" /></span>
										</div>
									</li>
								)
							})}
						</ul>
					</div>
				</div>
			</div>
		</>
	)
}

export default Hostroom;