import React, { useState, useRef, useEffect, use } from "react";
import Link from "next/link"

import Header from "../components/Header"
import Banner from "components/MypageBanner";
import Modal from "../components/UpdateRPDModal"
import PointModal from "../components/PointHistoryModal"
import Footer from "../components/Footer"
import Image from "next/image"
import img_profile from "/public/images/default-meeting-profile.svg"
import img_rpdance from "/public/images/dummy-rpdance.png"
import img_offline from "/public/images/icon-offline.png"
import img_board from "/public/images/icon-board.png"
import img_settings from "/public/images/icon-settings.svg"
import img_vector from "/public/images/icon-vector.png"

import { useRecoilState } from "recoil";
import { useRouter } from "next/router";
import { LanguageState } from "states/states";
import { accessTokenState, refreshTokenState, idState, nicknameState, profileImgState, rankNameState, canEditInfoState } from "states/states";

import axios from "axios";


const MyPage = () => {
  interface Boards {
    boardId: number,
    boardType: string,
    commentCnt: number,
    comments: string[],
    content: string,
    fileURL: string,
    profileImg: string,
    title: string,
    writerName: string,
  }

  const [lang, setLang] = useRecoilState(LanguageState);
  const [accessToken, setAccessToken] = useRecoilState(accessTokenState);
  const [refreshToken, setRefreshToken] = useRecoilState(refreshTokenState);
  const [id, setId] = useRecoilState(idState);
  const [loginUserNickname, setLoginUserNickname] = useRecoilState(nicknameState);
  const [profileImg, setProfileImg] = useRecoilState(profileImgState);
  const [rankName, setRankName] = useRecoilState(rankNameState);
  const [canEditInfo, setCanEditInfo] = useRecoilState(canEditInfoState);

  const [loginUser, setLoginUser] = useState<any>();
  const [goalRankPoint, setGoalRankPoint] = useState<number>(0);
  const [point, setPoint] = useState<number>(0);
  const [pointLeft, setPointLeft] = useState<number>();
  const [reserved, setReserved] = useState<any[]>();
  const [myRandomDance, setMyRandomDance] = useState<any[]>();
  const [myRandomDanceHistory, setMyRandomDanceHistory] = useState<any[]>();
  const [visibleItemsReserved, setVisibleItemsReserved] = useState(3);
  const [visibleItemsOpen, setVisibleItemsOpen] = useState(3);
  const [visibleItemsHistory, setVisibleItemsHistory] = useState(3);
  const [visibleItemsMeeting, setVisibleItemsMeeting] = useState(3);
  const [visibleItemsTalk, setVisibleItemsTalk] = useState(3);
  const [meetingBoard, setMeetingBoard] = useState<Boards[] | null>(null);
  const [talkBoard, setTalkBoard] = useState<Boards[] | null>(null);
  const [boardCnt, setBoardCnt] = useState<number>();
  const [readyData, setReadyData] = useState<boolean>(false);
  const [equalPw, setEqualPw] = useState<boolean>(true);
  const [checkPassword, setCheckPassword] = useState<any>();
  const [domLoaded, setDomLoaded] = useState(false);

  // 로그인한 유저가 개최한 랜플댄 정보 수정
  const [modalOpen, setModalOpen] = useState<boolean>(false);
  const [pointModalOpen, setPointModalOpen] = useState<boolean>(false);
  const [editId, setEditId] = useState<number>(0);
  const [editTitle, setEditTitle] = useState<string>('');
  const [editContent, setEditContent] = useState<string>('');
  const [editStartAt, setEditStartAt] = useState<any>();
  const [editEndAt, setEditEndAt] = useState<any>();
  const [editDanceType, setEditDanceType] = useState<string>('');
  const [editMaxUser, setEditMaxUser] = useState<number>();
  const [editThumbnail, setEditThumbnail] = useState<string>('');
  const [editHostId, setEditHostId] = useState<string>(id);
  const [editDanceMusicIdList, SetEditDanceMusicIdList] = useState<any>();
  const [rankBtnColor, setRankBtnColor] = useState<String>();

  const router = useRouter();
  const pwValue = useRef<HTMLInputElement>();

  // 작성한 게시글 목록으로 스크롤 이동
  const list = useRef<HTMLDivElement>(null);
  const scrollEvent = () => {
    list.current?.scrollIntoView({ behavior: "smooth", block: "start" });
  };

  // 개최한 랜플댄 정보 수정시 스크롤 이동
  const scrollToEditRPD = () => {
    window.scrollTo({ top: 0, behavior: "smooth" });
  };

  // 예약된 랜플댄 개수
  let reservedRandomDanceCnt: number = 0;

  // 작성한 게시글 개수 합산
  let talkCnt: number;
  let meetingCnt: number;

  useEffect(() => {
    if (id === '' || accessToken === '' || refreshToken === '' || loginUserNickname == '') {
      alert("로그인을 먼저 진행해주세요.");
      router.push('/login');
    } else {
      const setup = async () => {
        // 로그인 유저 정보 조회
        await axios.get("https://stepup-pi.com:8080/api/user", {
          headers: {
            Authorization: `Bearer ${accessToken}`
          }
        }).then((data) => {
          if (data.data.message === "회원정보 조회 완료") {
            setLoginUser(data.data.data);
            setLoginUserNickname(data.data.data.nickname);
            setRankName(data.data.data.rankName);

            const getRankColor = (rank: string) => {
              switch (rank) {
                case "BRONZE":
                  setRankBtnColor("#A77044");
                  setGoalRankPoint(100);
                  break;
                case "SILVER":
                  setRankBtnColor("#A7A7AD");
                  setGoalRankPoint(300);
                  break;
                case "GOLD":
                  setRankBtnColor("#FFB028");
                  setGoalRankPoint(1000);
                  break;
                case "PLATINUM":
                  setRankBtnColor("#86f989");
                  setGoalRankPoint(10000);
                  break;
              }
            }
            getRankColor(data.data.data.rankName);
            setProfileImg(data.data.data.profileImg);

            const getpoint = () => {
              if (typeof data.data.data.point === 'number') {
                setPoint(data.data.data.point);
              }
            }
            getpoint();
            setPointLeft(goalRankPoint - point);
          }
        }).catch((error: any) => {
          if (error.response.data.message === "만료된 토큰") {
            axios.get("https://stepup-pi.com:8080/api/user", {
              headers: {
                refreshToken: refreshToken,
              }
            }).then((data) => {
              if (data.data.message === "토큰 재발급 완료") {
                setAccessToken(data.data.data.accessToken);
                setRefreshToken(data.data.data.refreshToken);
              }
            }).then(() => {
              axios.get("https://stepup-pi.com:8080/api/user", {
                headers: {
                  Authorization: `Bearer ${accessToken}`
                }
              }).then((data) => {
                if (data.data.message === "회원정보 조회 완료") {
                  setLoginUser(data.data.data);
                  setLoginUserNickname(data.data.data.nickname);
                  setRankName(data.data.data.rankName);

                  const getRankColor = (rank: string) => {
                    switch (rank) {
                      case "BRONZE":
                        setRankBtnColor("#A77044");
                        setGoalRankPoint(100);
                        break;
                      case "SILVER":
                        setRankBtnColor("#A7A7AD");
                        setGoalRankPoint(300);
                        break;
                      case "GOLD":
                        setRankBtnColor("#FFB028");
                        setGoalRankPoint(1000);
                        break;
                      case "PLATINUM":
                        setRankBtnColor("#86f989");
                        setGoalRankPoint(10000);
                        break;
                    }
                  }
                  getRankColor(data.data.data.rankName);
                  setProfileImg(data.data.data.profileImg);

                  const getpoint = () => {
                    if (typeof data.data.data.point === 'number') {
                      setPoint(data.data.data.point);
                    }
                  }
                  getpoint();
                  setPointLeft(goalRankPoint - point);
                }
              }).catch((data) => {
                if (data.response.status === 401) {
                  alert("장시간 이용하지 않아 자동 로그아웃 되었습니다.");
                  router.push("/login");
                  return;
                }

                if (data.response.status === 500) {
                  alert("시스템 에러, 관리자에게 문의하세요.");
                  return;
                }
              })
            })
          }
        })

        // 로그인 유저가 작성한 정모 게시글 조회
        await axios.get(`https://stepup-pi.com:8080/api/board/meeting/my`, {
          headers: {
            Authorization: `Bearer ${accessToken}`
          }
        }).then((data) => {
          if (data.data.message === "내가 작성한 정모 목록 조회") {
            setMeetingBoard(data.data.data as Boards[]);
            meetingCnt = data.data.data.length!;
          }
        }).catch((error: any) => {
          if (error.response.data.message === "만료된 토큰") {
            axios.get(`https://stepup-pi.com:8080/api/board/meeting/my`, {
              headers: {
                refreshToken: refreshToken
              }
            }).then((data) => {
              if (data.data.message === "토큰 재발급 완료") {
                setAccessToken(data.data.data.accessToken);
                setRefreshToken(data.data.data.refreshToken);
              }
            }).then(() => {
              axios.get(`https://stepup-pi.com:8080/api/board/meeting/my`, {
                headers: {
                  Authorization: `Bearer ${accessToken}`
                }
              }).then((data) => {
                if (data.data.message === "내가 작성한 정모 목록 조회") {
                  setMeetingBoard(data.data.data as Boards[]);
                  meetingCnt = data.data.data.length!;
                }
              }).catch((data) => {
                if (data.response.status === 401) {
                  alert("장시간 이용하지 않아 자동 로그아웃 되었습니다.");
                  router.push("/login");
                  return;
                }

                if (data.response.status === 500) {
                  alert("시스템 에러, 관리자에게 문의하세요.");
                  return;
                }
              })
            })
          }
        })

        // 로그인 유저가 작성한 자유게시판 게시글 조회
        await axios.get(`https://stepup-pi.com:8080/api/board/talk/my`, {
          headers: {
            Authorization: `Bearer ${accessToken}`
          }
        }).then((data) => {
          if (data.data.message === "내가 작성한 자유게시판 목록 조회") {
            setTalkBoard(data.data.data as Boards[]);
            talkCnt = data.data.data.length!;
            setBoardCnt(talkCnt + meetingCnt);
          }
        }).catch((error: any) => {
          if (error.response.data.message === "만료된 토큰") {
            axios.get(`https://stepup-pi.com:8080/api/board/talk/my`, {
              headers: {
                refreshToken: refreshToken
              }
            }).then((data) => {
              if (data.data.message === "토큰 재발급 완료") {
                setAccessToken(data.data.data.accessToken);
                setRefreshToken(data.data.data.refreshToken);
              }
            }).then(() => {
              axios.get(`https://stepup-pi.com:8080/api/board/talk/my`, {
                headers: {
                  Authorization: `Bearer ${accessToken}`
                }
              }).then((data) => {
                if (data.data.message === "내가 작성한 자유게시판 목록 조회") {
                  setTalkBoard(data.data.data as Boards[]);
                  talkCnt = data.data.data.length!;
                  setBoardCnt(talkCnt + meetingCnt);
                }
              }).catch((data) => {
                if (data.response.status === 401) {
                  alert("장시간 이용하지 않아 자동 로그아웃 되었습니다.");
                  router.push("/login");
                  return;
                }

                if (data.response.status === 500) {
                  alert("시스템 에러, 관리자에게 문의하세요.");
                  return;
                }
              })
            })
          }
        })

        // 로그인 유저의 랜플댄 예약 목록 조회
        await axios.get("https://stepup-pi.com:8080/api/dance/my/reserve", {
          headers: {
            Authorization: `Bearer ${accessToken}`
          }
        }).then((data) => {
          if (data.data.message === "내가 예약한 랜덤 플레이 댄스 목록 조회 완료") {
            setReserved(data.data.data);
            reservedRandomDanceCnt = reserved?.length!;
          }
        }).catch((error: any) => {
          if (error.response.data.message === "만료된 토큰") {
            axios.get("https://stepup-pi.com:8080/api/dance/my/reserve", {
              headers: {
                refreshToken: refreshToken
              }
            }).then((data) => {
              if (data.data.message === "토큰 재발급 완료") {
                setAccessToken(data.data.data.accessToken);
                setRefreshToken(data.data.data.refreshToken);
              }
            }).then(() => {
              axios.get("https://stepup-pi.com:8080/api/dance/my/reserve", {
                headers: {
                  Authorization: `Bearer ${accessToken}`
                }
              }).then((data) => {
                if (data.data.message === "내가 예약한 랜덤 플레이 댄스 목록 조회 완료") {
                  setReserved(data.data.data);
                  reservedRandomDanceCnt = reserved?.length!;
                }
              }).catch((data) => {
                if (data.response.status === 401) {
                  alert("장시간 이용하지 않아 자동 로그아웃 되었습니다.");
                  router.push("/login");
                  return;
                }

                if (data.response.status === 500) {
                  alert("시스템 에러, 관리자에게 문의하세요.");
                  return;
                }
              })
            })
          }
        })

        // 로그인 유저가 개최한 랜플댄 목록 조회
        await axios.get("https://stepup-pi.com:8080/api/dance/my/open", {
          headers: {
            Authorization: `Bearer ${accessToken}`
          }
        }).then((data) => {
          if (data.data.message === "내가 개최한 랜덤 플레이 댄스 목록 조회 완료") {
            setMyRandomDance(data.data.data);
          }
        }).catch((error: any) => {
          if (error.response.data.message === "만료된 토큰") {
            axios.get("https://stepup-pi.com:8080/api/dance/my/open", {
              headers: {
                refreshToken: refreshToken
              }
            }).then((data) => {
              if (data.data.message === "토큰 재발급 완료") {
                setAccessToken(data.data.data.accessToken);
                setRefreshToken(data.data.data.refreshToken);
              }
            }).then((data) => {
              axios.get("https://stepup-pi.com:8080/api/dance/my/open", {
                headers: {
                  Authorization: `Bearer ${accessToken}`
                }
              }).then((data) => {
                if (data.data.message === "내가 개최한 랜덤 플레이 댄스 목록 조회 완료") {
                  setMyRandomDance(data.data.data);
                }
              }).catch((data) => {
                if (data.response.status === 401) {
                  alert("장시간 이용하지 않아 자동 로그아웃 되었습니다.");
                  router.push("/login");
                  return;
                }

                if (data.response.status === 500) {
                  alert("시스템 에러, 관리자에게 문의하세요.");
                  return;
                }
              })
            })
          }
        })

        // 로그인 유저가 참여한 랜플댄 목록 조회
        await axios.get("https://stepup-pi.com:8080/api/dance/my/attend/", {
          headers: {
            Authorization: `Bearer ${accessToken}`
          },
        }).then((data) => {
          if (data.data.message === "내가 참여한 랜덤 플레이 댄스 목록 조회 완료") {
            setMyRandomDanceHistory(data.data.data);
          }
        }).catch((error: any) => {
          if (error.response.data.message === "만료된 토큰") {
            axios.get("https://stepup-pi.com:8080/api/dance/my/attend/", {
              headers: {
                refreshToken: refreshToken
              },
            }).then((data) => {
              if (data.data.message === "토큰 재발급 완료") {
                setAccessToken(data.data.data.accessToken);
                setRefreshToken(data.data.data.refreshToken);
              }
            }).then(() => {
              axios.get("https://stepup-pi.com:8080/api/dance/my/attend/", {
                headers: {
                  Authorization: `Bearer ${accessToken}`
                },
              }).then((data) => {
                if (data.data.message === "내가 참여한 랜덤 플레이 댄스 목록 조회 완료") {
                  setMyRandomDanceHistory(data.data.data);
                }
              }).catch((data) => {
                if (data.response.status === 401) {
                  alert("장시간 이용하지 않아 자동 로그아웃 되었습니다.");
                  router.push("/login");
                  return;
                }

                if (data.response.status === 500) {
                  alert("시스템 에러, 관리자에게 문의하세요.");
                  return;
                }
              })
            })
          }
        })

        await setReadyData(true);
        await setDomLoaded(true);
      }
      setup();
    }
  }, []);

  // 로그인 유저의 랜플댄 예약 취소
  const cancelRandomDance = async (selectedId: any) => {
    await axios.delete(`https://stepup-pi.com:8080/api/dance/my/reserve/${selectedId.reservationId}`, {
      headers: {
        Authorization: `Bearer ${accessToken}`
      }
    }).then((data) => {
      if (data.data.message === "랜덤 플레이 댄스 예약 취소 완료") {
        alert("예약을 취소하셨습니다.")
        window.location.replace("/mypage");
      } else {
        alert("예약을 취소하지 못했습니다. 다시 한 번 시도해주세요.")
      }
    }).catch((error: any) => {
      if (error.response.data.message === "만료된 토큰") {
        axios.delete(`https://stepup-pi.com:8080/api/dance/my/reserve/${selectedId.reservationId}`, {
          headers: {
            refreshToken: refreshToken
          }
        }).then((data) => {
          if (data.data.message === "토큰 재발급 완료") {
            setAccessToken(data.data.data.accessToken);
            setRefreshToken(data.data.data.refreshToken);
          }
        }).then(() => {
          axios.delete(`https://stepup-pi.com:8080/api/dance/my/reserve/${selectedId.reservationId}`, {
            headers: {
              Authorization: `Bearer ${accessToken}`
            }
          }).then((data) => {
            if (data.data.message === "랜덤 플레이 댄스 예약 취소 완료") {
              alert("예약을 취소하셨습니다.")
              window.location.replace("/mypage");
            } else {
              alert("예약을 취소하지 못했습니다. 다시 한 번 시도해주세요.")
            }
          }).catch((data) => {
            if (data.response.status === 401) {
              alert("장시간 이용하지 않아 자동 로그아웃 되었습니다.");
              router.push("/login");
              return;
            }

            if (data.response.status === 500) {
              alert("시스템 에러, 관리자에게 문의하세요.");
              return;
            }
          })
        })
      }
    })
  }

  // 로그인 유저가 개최한 랜플댄 삭제
  const deleteMyRandomDance = async (roomid: any) => {
    await axios.delete(`https://stepup-pi.com:8080/api/dance/my/${roomid.rpdId}`, {
      headers: {
        Authorization: `Bearer ${accessToken}`
      },
    }).then((data) => {
      if (data.data.message === "랜덤 플레이 댄스 삭제 완료") {
        alert("개최한 랜덤 플레이 댄스를 삭제했습니다.");
        window.location.replace("/mypage");
      } else {
        alert("삭제를 하지 못했습니다. 다시 한 번 시도해주세요.");
      }
    }).catch((error: any) => {
      if (error.response.data.message === "만료된 토큰") {
        axios.delete(`https://stepup-pi.com:8080/api/dance/my/${roomid.rpdId}`, {
          headers: {
            refreshToken: refreshToken
          },
        }).then((data) => {
          if (data.data.message === "토큰 재발급 완료") {
            setAccessToken(data.data.data.accessToken);
            setRefreshToken(data.data.data.refreshToken);
          }
        }).then(() => {
          axios.delete(`https://stepup-pi.com:8080/api/dance/my/${roomid.rpdId}`, {
            headers: {
              Authorization: `Bearer ${accessToken}`
            },
          }).then((data) => {
            if (data.data.message === "랜덤 플레이 댄스 삭제 완료") {
              alert("개최한 랜덤 플레이 댄스를 삭제했습니다.");
              window.location.replace("/mypage");
            } else {
              alert("삭제를 하지 못했습니다. 다시 한 번 시도해주세요.");
            }
          }).catch((data) => {
            if (data.response.status === 401) {
              alert("장시간 이용하지 않아 자동 로그아웃 되었습니다.");
              router.push("/login");
              return;
            }

            if (data.response.status === 500) {
              alert("시스템 에러, 관리자에게 문의하세요.");
              return;
            }
          })
        })
      }
    })
  }

  // 비밀번호 일치 확인
  const checkPw = async () => {
    setCheckPassword(pwValue.current!.value);
    try {
      axios.post("https://stepup-pi.com:8080/api/user/checkpw", {
        id: id,
        password: pwValue.current!.value,
      }, {
        headers: {
          Authorization: `Bearer ${accessToken}`,
        }
      }).then((data) => {
        if (data.data.message === "비밀번호 일치") {
          setEqualPw(true);
          setCanEditInfo(true);
          router.push('/mypageedit');
        }
      }).catch((error: any) => {
        setEqualPw(false);
        if (error.response.data.message === "만료된 토큰") {
          axios.post("https://stepup-pi.com:8080/api/user/checkpw", {
            id: id,
            password: pwValue.current!.value,
          }, {
            headers: {
              refreshToken: refreshToken
            }
          }).then((data) => {
            if (data.data.message === "토큰 재발급 완료") {
              setAccessToken(data.data.data.accessToken);
              setRefreshToken(data.data.data.refreshToken);
            }
          }).then(() => {
            axios.post("https://stepup-pi.com:8080/api/user/checkpw", {
              id: id,
              password: pwValue.current!.value,
            }, {
              headers: {
                Authorization: `Bearer ${accessToken}`,
              }
            }).then((data) => {
              if (data.data.message === "비밀번호 일치") {
                setEqualPw(true);
                setCanEditInfo(true);
                router.push('/mypageedit');
              }
            }).catch((data) => {
              if (data.response.status === 401) {
                alert("장시간 이용하지 않아 자동 로그아웃 되었습니다.");
                router.push("/login");
                return;
              }

              if (data.response.status === 500) {
                alert("시스템 에러, 관리자에게 문의하세요.");
                return;
              }
            })
          })
        }
      })
    } catch (e) {
    }
  }

  // 랜플댄 수정 모달창
  const leaveModalOpen = async (randomDance: any) => {
    setEditId(randomDance.randomDanceId);
    setEditTitle(randomDance.title);
    setEditContent(randomDance.content);
    setEditStartAt(randomDance.startAt);
    setEditEndAt(randomDance.endAt);
    setEditDanceType(randomDance.danceType);
    setEditMaxUser(randomDance.maxUser);
    setEditThumbnail(randomDance.thumbnail);
    setEditHostId(randomDance.hostId);
    SetEditDanceMusicIdList(randomDance.danceMusicIdList);
    scrollToEditRPD();
    setModalOpen(true);
  }
  const leaveModalClose = async () => {
    setModalOpen(false);
  }

  // 포인트 적립 내역 모달창
  const leavePointModalOpen = async () => {
    setPointModalOpen(true);
  }
  const leavePointModalClose = async () => {
    setPointModalOpen(false);
  }

  return (
    <>
      {domLoaded ? (
        <div>
          <Header />
          <Banner />
          <div className="ce">
            <div className="background-color">
              <div>
                <ul className="mypage-profile">
                  <li>
                    <div className="info">
                      <div className="cnt">{readyData ? reservedRandomDanceCnt : null}</div>
                      <div className="cnt-title">{lang === "en" ? "RESERVATION" : lang === "cn" ? "预订" : "예약된 랜플댄"}</div>
                      <div className="btn-info">
                        <Link href="/randomplay/list">{lang === "en" ? "RPDance LIST" : lang === "cn" ? "舞蹈清单" : "랜플댄 목록 보기"}</Link>
                      </div>
                    </div>
                  </li>
                  <li>
                    <div className="info history">
                      <div className="img-profile">
                        {profileImg === null || profileImg === 'url' || profileImg == ""
                          ? (<Image onClick={leavePointModalOpen} className="img" src={img_profile} alt="profile_default" width={100} height={100}></Image>)
                          : (<Image onClick={leavePointModalOpen} className="img" src={profileImg.toString()} alt="profile" width={100} height={100}></Image>)}
                      </div>
                      <div>
                        <progress value={point} max={goalRankPoint}></progress>
                        <div className="progress-text">
                          <p>{lang === "en" ? "the next rank" : lang === "cn" ? "直到下一次排名" : "다음 랭킹까지"} {Number(goalRankPoint - point)}</p>
                          {readyData ? (<p>{point}/{goalRankPoint}</p>) : (<p></p>)}
                        </div>
                        <div className="info-user">
                          <p className="ranking" style={{ backgroundColor: rankBtnColor }}>{rankName}</p>
                          <p className="name">{loginUserNickname}</p>
                        </div>
                      </div>
                    </div>
                  </li>
                  <li>
                    <div className="info">
                      <div className="cnt">{boardCnt}</div>
                      <div className="cnt-title">{lang === "en" ? "Number of POSTS" : lang === "cn" ? "撰写的帖子数量" : "작성한 글 수"}</div>
                      <div className="btn-info">
                        <div onClick={scrollEvent}>{lang === "en" ? "CHECK POSTS" : lang === "cn" ? "检查帖子" : "게시글 확인하기"}</div>
                      </div>
                    </div>
                  </li>
                </ul>
              </div>
              {/* end - profile information */}
              <div>
                <ul className="list">
                  <div>{lang === "en" ? "My Reservation" : lang === "cn" ? "我的预订" : "내 예약"}</div>
                  <div>
                    {reserved?.slice(0, visibleItemsReserved)?.map((reservation, index) => {
                      const reservationId = reservation.randomDanceId;
                      return (
                        <li className="contents" key={index}>
                          <div className="img-box">
                            <Image className="img" src={img_rpdance} alt="reserved" width={100} height={100}></Image>
                          </div>
                          <div className="description">
                            <div className="time">Korea {reservation.startAt} ~ {reservation.endAt} (KST)</div>
                            <div className="title">{reservation.title}</div>
                            <div className="learner">{reservation.danceType}</div>
                          </div>
                          <div className="btn-contents">
                            <a onClick={() => cancelRandomDance({ reservationId })}>{lang === "en" ? "Cancel" : lang === "cn" ? "取消参与" : "예약 취소"}</a>
                          </div>
                        </li>
                      )
                    })}
                  </div>
                  {reserved && visibleItemsReserved < reserved.length && (
                    <button className="btn-more" onClick={() => setVisibleItemsReserved(visibleItemsReserved + 3)}>
                      {lang === "en" ? "View More" : lang === "cn" ? "查看更多" : "더보기"}
                    </button>
                  )}
                </ul>
              </div>
              {/* end - my reservation */}
              <div>
                <ul className="list">
                  <div>{lang === "en" ? "My Organized" : lang === "cn" ? "我举办的活动" : "개최한 랜플댄"}</div>
                  <div>
                    {myRandomDance?.slice(0, visibleItemsOpen)?.map((randomDance, index) => {
                      const rpdId: number = randomDance.randomDanceId;
                      return (
                        <li className="contents" key={index}>
                          <div className="img-box">
                            <Image className="img" src={img_rpdance} alt="open" width={100} height={100}></Image>
                          </div>
                          <div className="description">
                            <div className="time">Korea {randomDance.startAt} ~ {randomDance.endAt} (KST)</div>
                            <div className="title">{randomDance.title}</div>
                            <div className="learner">{randomDance.danceType}</div>
                          </div>
                          <div className="btn-contents">
                            <a onClick={() => leaveModalOpen(randomDance)}>{lang === "en" ? "Edit" : lang === "cn" ? "更改信息" : "방 정보 수정"}</a>
                            <a onClick={() => deleteMyRandomDance({ rpdId })}>{lang === "en" ? "Host - Cancel" : lang === "cn" ? "取消活动" : "방 생성 취소"}</a>
                          </div>
                        </li>
                      )
                    })}
                  </div>
                  {myRandomDance && visibleItemsOpen < myRandomDance.length && (
                    <button className="btn-more" onClick={() => setVisibleItemsOpen(visibleItemsOpen + 3)}>
                      {lang === "en" ? "View More" : lang === "cn" ? "查看更多" : "더보기"}
                    </button>
                  )}
                </ul>
              </div>
              {/* end - my open */}
              <div className="settings">
                <details>
                  <summary>
                    <Image src={img_settings} className="img-setting" alt="img_settings"></Image>
                    <p className="settings-title">{lang === "en" ? "Edit My Info" : lang === "cn" ? "编辑会员信息" : "회원 정보 수정"}</p>
                    <Image className="img-vector" src={img_vector} alt="img_arrow"></Image>
                  </summary>
                  <div className="enter-password">
                    <p>{lang === "en" ? "Password" : lang === "cn" ? "密码" : "현재 비밀번호"}</p>
                    <input className="pw" type="password" ref={pwValue} />
                    <div className="btn-submit" onClick={checkPw}>
                      {lang === "en" ? "Enter" : lang === "cn" ? "输入" : "입력"}
                    </div>
                  </div>
                  {equalPw ? (<p> </p>) : (<p className="notequal">비밀번호가 일치하지 않습니다. 다시 입력해주세요.</p>)}
                </details>
              </div>
              {/* end - settings */}
              <div>
                <ul className="list">
                  <div>{lang === "en" ? "Participation History" : lang === "cn" ? "参与历史" : "참여 이력"}</div>
                  <div>
                    {myRandomDanceHistory?.slice(0, visibleItemsHistory)?.map((randomDance, index) => {
                      const randomDanceId = randomDance.randomDanceId;
                      return (
                        <li className="contents" key={index}>
                          <div className="img-box">
                            <Image className="img" src={img_rpdance} alt="open" width={100} height={100}></Image>
                          </div>
                          <div className="description">
                            <div className="time">Korea {randomDance.startAt} ~ {randomDance.endAt} (KST)</div>
                            <div className="title">{randomDance.title}</div>
                            <div className="learner">{randomDance.danceType}</div>
                          </div>
                          <div className="rank">
                            <p>-</p>
                          </div>
                        </li>
                      )
                    })}
                  </div>
                  {myRandomDanceHistory && visibleItemsHistory < myRandomDanceHistory.length && (
                    <button className="btn-more" onClick={() => setVisibleItemsHistory(visibleItemsHistory + 3)}>
                      {lang === "en" ? "View More" : lang === "cn" ? "查看更多" : "더보기"}
                    </button>
                  )}
                </ul>
              </div>
              {/* end - past random play dance */}
              <div ref={list}>
                <ul className="list">
                  <div>{lang === "en" ? "Written Posts - Offline Meetings" : lang === "cn" ? "撰写的帖子 - 线下聚会" : "작성한 글 - 오프라인 정모"}</div>
                  <div>
                    {meetingBoard?.slice(0, visibleItemsMeeting)?.map((board, index) => {
                      const linkUrl = `/meeting/detail/${board.boardId}`;
                      return (
                        <li className="contents" key={index}>
                          <div className="img-box">
                            <Image className="img" src={img_offline} alt="history" width={100} height={100}></Image>
                          </div>
                          <div className="description">
                            <div className="time">{lang === "en" ? "Offline Meetings" : lang === "cn" ? "线下聚会" : "오프라인 정모"}</div>
                            <div className="title-past">{board.title}</div>
                            <div className="learner">comment {board.commentCnt}</div>
                          </div>
                          <div className="detail">
                            <a><Link href={linkUrl}>{lang === "en" ? "View" : lang === "cn" ? "查看帖子" : "글 보기"}</Link></a>
                          </div>
                        </li>
                      )
                    })}
                  </div>
                  {meetingBoard && visibleItemsMeeting < meetingBoard.length && (
                    <button className="btn-more" onClick={() => setVisibleItemsMeeting(visibleItemsMeeting + 3)}>
                      {lang === "en" ? "View More" : lang === "cn" ? "查看更多" : "더보기"}
                    </button>
                  )}
                </ul>
                {/* end - meeting board */}
                <ul className="list">
                  <div>{lang === "en" ? "Written Posts - Free Talk" : lang === "cn" ? "撰写的帖子 - 自由留言板" : "작성한 글 - 자유게시판"}</div>
                  <div>
                    {talkBoard?.slice(0, visibleItemsTalk)?.map((board, index) => {
                      const linkUrl = `/article/detail/${board.boardId}`;
                      return (
                        <li className="contents" key={index}>
                          <div className="img-box">
                            <Image className="img" src={img_board} alt="history" width={100} height={100}></Image>
                          </div>
                          <div className="description">
                            <div className="time">{lang === "en" ? "Offline Meetings" : lang === "cn" ? "线下聚会" : "오프라인 정모"}</div>
                            <div className="title-past">{board.title}</div>
                            <div className="learner">comment {board.commentCnt}</div>
                          </div>
                          <div className="detail">
                            <a><Link href={linkUrl}>{lang === "en" ? "View" : lang === "cn" ? "查看帖子" : "글 보기"}</Link></a>
                          </div>
                        </li>
                      )
                    })}
                  </div>
                  {talkBoard && visibleItemsTalk < talkBoard.length && (
                    <button className="btn-more" onClick={() => setVisibleItemsTalk(visibleItemsTalk + 3)}>
                      {lang === "en" ? "View More" : lang === "cn" ? "查看更多" : "더보기"}
                    </button>
                  )}
                </ul>
                {/* end - board */}
              </div>
              <div className="ad-enjoy">
                <div className="ad-title">
                  <span>{lang === "en" ? "STEP UP " : lang === "cn" ? "STEP UP " : "STEP UP의 "}</span>
                  <span>{lang === "en" ? "Enjoy More Attractions" : lang === "cn" ? "更多乐趣体验" : "더 많은 즐길거리 즐기기"}</span>
                </div>
                <div className="ad-btn">
                  <a id="ad-whitebox"><Link href="/randomplay/list">{lang === "en" ? "Reservation" : lang === "cn" ? "预约" : "랜플댄 예약하기"}</Link></a>
                  <a id="ad-bluebox"><Link href="/practiceroom">{lang === "en" ? "Practice Room" : lang === "cn" ? "使用练习室" : "개인연습실 이용하기"}</Link></a>
                </div>
              </div>
            </div>
            {/* end - advertisement */}
          </div>
          {modalOpen &&
            <Modal open={modalOpen} close={leaveModalClose} randomDanceId={editId} title={editTitle} content={editContent} startAt={editStartAt} endAt={editEndAt} danceType={editDanceType} maxUser={Number(editMaxUser)} thumbnail={editThumbnail} hostId={editHostId} danceMusicIdList={editDanceMusicIdList}></Modal>
          }
          {pointModalOpen &&
            <PointModal open={pointModalOpen} close={leavePointModalClose}></PointModal>
          }
          <Footer />
        </div>
      )
        : (<div></div>)}
    </>
  )
}

export default MyPage;