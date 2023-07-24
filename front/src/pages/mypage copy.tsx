import Header from "../components/Header"
import Footer from "../components/Footer"
import Image from "next/image"
import img_profile from "/public/images/dummy-profile.png"
import img_rpdance from "/public/images/dummy-rpdance.png"
import img_notice from "/public/images/icon-notice.png"
import img_offline from "/public/images/icon-offline.png"
import img_requestsong from "/public/images/icon-requestsong.png"
import img_board from "/public/images/icon-board.png"

const MyPage = () => {
  return (
    <>
      <Header />
      <div className="background-color">
        <div>
          <ul className="mypage-profile">
            <li>
              <div className="info">
                <div className="cnt">3</div>
                <div className="cnt-title">예약된 랜플댄</div>
                <div className="btn-info">
                  <a href="">랜플댄 시간표 보기</a>
                </div>
              </div>
            </li>
            <li>
              <div className="info">
                <div className="img-profile">
                  <Image className="img" src={img_profile} alt="profile"></Image>
                </div>
                <div>
                  <progress value="985" max="2000"></progress>
                  <div className="progress-text">
                    <p>다음 랭킹까지 1,015</p>
                    <p>985/2,000</p>
                  </div>
                  <div className="info-user">
                    <p className="ranking">골드</p>
                    <p className="name">김싸피</p>
                  </div>
                </div>
              </div>
            </li>
            <li>
              <div className="info">
                <div className="cnt">3</div>
                <div className="cnt-title">작성한 글 수</div>
                <div className="btn-info">
                  <a href="">게시글 확인하기</a>
                </div>
              </div>
            </li>
          </ul>
        </div>
        {/* end - profile information */}
        <div>
          <ul className="list">
            <div>내 예약</div>
            <li className="contents">
              <div className="img-box">
                <Image className="img" src={img_rpdance} alt="reserved"></Image>
              </div>
              <div className="description">
                <div className="time">Korea 5:00PM ~ 6:20PM (KST)</div>
                <div className="title">여기서요? 4세대 남돌 곡 모음</div>
                <div className="learner">Learner</div>
              </div>
              <div className="btn-contents">
                <a href="">예약 취소</a>
                <a href="">방 생성 취소</a>
              </div>
            </li>
            {/* end - one reservation*/}
            <li className="contents">
              <div className="img-box">
                <Image className="img" src={img_rpdance} alt="reserved"></Image>
              </div>
              <div className="description">
                <div className="time">Korea 5:00PM ~ 6:20PM (KST)</div>
                <div className="title">여기서요? 4세대 남돌-여돌 곡 모음 제목이 길면 다음줄로. 최대 2줄</div>
                <div className="learner">Learner</div>
              </div>
              <div className="btn-contents">
                <a href="">예약 취소</a>
                <a href="">방 생성 취소</a>
              </div>
            </li>
            <li className="contents">
              <div className="img-box">
                <Image className="img" src={img_rpdance} alt="reserved"></Image>
              </div>
              <div className="description">
                <div className="time">Korea 5:00PM ~ 6:20PM (KST)</div>
                <div className="title">여기서요? 4세대 남돌-여돌 곡 모음 제목이 길면 다음줄로. 최대 2줄</div>
                <div className="learner">Learner</div>
              </div>
              <div className="btn-contents">
                <a href="">예약 취소</a>
                <a href="">방 생성 취소</a>
              </div>
            </li>
          </ul>
        </div>
        {/* end - my reservation */}
        <div className="settings">
          <details>
            <summary>
              <div className="img-setting"></div>
              <p className="settings-title">회원 정보 수정</p>
              <div className="img-vector"></div>
            </summary>
            <div className="enter-password">
              <p>현재 비밀번호</p>
              <input className="pw" type="password" />
              <input className="btn-submit" type="submit" />
            </div>
          </details>
        </div>
        {/* end - settings */}
        <div>
          <ul className="list">
            <div>참여 이력</div>
            <li className="contents">
              <div className="img-box">
                <Image className="img" src={img_rpdance} alt="reserved"></Image>
              </div>
              <div className="description">
                <div className="time">Korea 5:00PM ~ 6:20PM (KST)</div>
                <div className="title-past">여기서요? 4세대 남돌 곡 모음</div>
                <div className="learner">Learner</div>
              </div>
              <div className="rank">
                <p>-</p>
              </div>
            </li>
            {/* end - one past random play dance*/}
            <li className="contents">
              <div className="img-box">
                <Image className="img" src={img_rpdance} alt="reserved"></Image>
              </div>
              <div className="description">
                <div className="time">Korea 5:00PM ~ 6:20PM (KST)</div>
                <div className="title-past">여기서요? 4세대 남돌-여돌 곡 모음 제목이 길면 다음줄로. 최대 2줄</div>
                <div className="learner">Learner</div>
              </div>
              <div className="rank">
                <p>1위</p>
              </div>
            </li>
            <li className="contents">
              <div className="img-box">
                <Image className="img" src={img_rpdance} alt="reserved"></Image>
              </div>
              <div className="description">
                <div className="time">Korea 5:00PM ~ 6:20PM (KST)</div>
                <div className="title-past">여기서요? 4세대 남돌-여돌 곡 모음 제목이 길면 다음줄로. 최대 2줄</div>
                <div className="learner">Learner</div>
              </div>
              <div className="rank">
                <p>3위</p>
              </div>
            </li>
            <a href="" className="btn-more">더보기</a>
          </ul>
        </div>
        {/* end - past random play dance */}
        <div>
          <ul className="list">
            <div>작성한 글</div>
            <li className="contents">
              <div className="img-box">
                <Image className="img" src={img_notice} alt="notice"></Image>
              </div>
              <div className="description">
                <div className="time">공지사항</div>
                <div className="title-past">작성한 공지사항 제목</div>
                <div className="learner">2023.07.15</div>
              </div>
              <div className="detail">
                <a href="">글 보기</a>
              </div>
            </li>
            {/* end - one board*/}
            <li className="contents">
              <div className="img-box">
                <Image className="img" src={img_requestsong} alt="song"></Image>
              </div>
              <div className="description">
                <div className="time">플레이리스트</div>
                <div className="title-past">작성한 노래 신청글 제목 / 제목이 길면 다음줄로. 최대 2줄</div>
                <div className="learner">2023.07.15</div>
              </div>
              <div className="detail">
                <a href="">글 보기</a>
              </div>
            </li>
            <li className="contents">
              <div className="img-box">
                <Image className="img" src={img_offline} alt="reserved"></Image>
              </div>
              <div className="description">
                <div className="time">오프라인 정모</div>
                <div className="title-past">작성한 오프라인 정모 게시글 제목 / 제목이 길면 다음줄로. 최대 2줄</div>
                <div className="learner">2023.07.15</div>
              </div>
              <div className="detail">
                <a href="">글 보기</a>
              </div>
            </li>
            <a href="" className="btn-more">더보기</a>
          </ul>
          {/* end - board */}
        </div>
        <div className="ad-enjoy">
          <div className="ad-title">
            <span>STEPUP의 </span>
            <span>더 많은 즐길거리 즐기기</span>
          </div>
          <div className="ad-btn">
            <a id="ad-whitebox" href="">랜플댄 예약하기</a>
            <a id="ad-bluebox" href="">개인연습실 이용하기</a>
          </div>
        </div>
      </div>
      {/* end - advertisement */}
      <Footer />
    </>
  )
}

export default MyPage;