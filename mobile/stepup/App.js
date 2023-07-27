import { StyleSheet, View, Image, FlatList } from 'react-native';

import MainBanner from "/assets/images/mo-main-img.png";
import HamburgerMenu from "/assets/images/icon-hamburger-menu.svg";
import MotionIcon from "/assets/images/icon-motion.svg";
import GroupIcon from "/assets/images/icon-group.svg";
import MusicNoteIcon from "/assets/images/icon-music-note.svg";
import ShareScreenIcon from "/assets/images/icon-share-screen.svg";
import RankIcon from "/assets/images/icon-rank.svg";

const iconList = [
  {id:1, iconImage: {MotionIcon}},
  {id:2, iconImage: {GroupIcon}},
  {id:3, iconImage: {ShareScreenIcon}},
  {id:4, iconImage: {MusicNoteIcon}},
  {id:5, iconImage: {RankIcon}},
];

export default function App() {
  return (
    <View style={styles.container}>
      <View style={styles.header}>
        <Image source={HamburgerMenu} alt=""/>
        <h1 style={styles.logo}><Link href="">STEP UP</Link></h1>
      </View>
      <View style={styles.mainBanner}>

      </View>
      <View style={styles.meeting}>
        <Text style={styles.meetingTitle}>
          KPOP에 필요한<br/>
          모든 만남이<br/>
          있는 곳
        </Text>
        <FlatList
            data = {iconList}
            keyExtractor={iconList => iconList.id.toString()}
            renderItem={({item}) => {
              <li>{item.iconImage}</li>
            }}
          />
      </View>
      <View style={styles.info}>
        <Text style={styles.infoTitle}>
          언제, 어디서든 랜덤한
          KPOP이 함께 나오는 곳
        </Text>
        <Text style={styles.infoInfo}>
          언제, 어디서든 장소에 구애받지 않고 원하는 춤을 출 수 있어요
          당신의 시간에 맞게 랜플댄 무대가 제공됩니다.
        </Text>
        <Text style={styles.infoLanding}>
          랜덤플레이댄스 참여하기
        </Text>
      </View>
      <View style={styles.realtime}>
        <View>
          <Text style={styles.realtimeTitle}>실시간 인기 랜플댄</Text>
          <Text style={styles.realtimeInfo}>현재 가장 인기가 많은 랜덤플레이댄스방에 참여해보세요.</Text>
        </View>
        <View>
          <View>
            <View>

            </View>
            <View>
              <Text>여기서요? 4세대 남돌·여돌 곡 모음</Text>
              <Text>관심 26 | 참여 AM10시 ~ PM3시 </Text>
            </View>
          </View>
        </View>
      </View>
      <View>
        <View style={styles.footerNav}>
          <Text>회사소개</Text>
          <Text>개인정보처리방침</Text>
          <Text>이용약관</Text>
          <Text>이용안내</Text>
        </View>
        <View>Footer 로고</View>
        <Text>기기 종류와 관계없이 모든 사용자에게 안전하고 품질이 우수한 화상 회의와 영상 통화 기능을 제공하는 서비스입니다 </Text>
        <Text>Copyright 2017. SSAFY. All rights reserved.</Text>
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
  },
  header:{

  },
  logo:{
    fontSize: "14px",
    fontWeight: "700",
    color: "#E2EFFF",
  },
  mainBanner:{
    backgroundImage: {MainBanner},
    backgroundPosition: "center",
    backgroundSize: "cover",
  },
  meeting:{
    padding:"40px",
    boxSizing: "border-box",
  },
  meetingTitle:{
    fontSize: "22px",
    fontWeight: "900",
    color: "#414149",
  },
  info:{
    
  },
  infoTitle: {
    fontSize: "22px",
    fontWeight: "700",
    color: "#3F4253",
  },
  infoInfo: {
    fontSize: "11px",
    fontWeight: "400",
    color: "#92969F", 
  },
  infoLanding: {
    fontSize: "12px",
    fontWeight:"300",
    color: "#506DF9",
  },
  footerNav: {
    fontSize:"12px",
    fontWeight:"700",
    color: "#999999",
  },
  footerLogo:{
    fontSize: "26px",
    fonWeight:"700",
    color:"#7E7E7E",
  },
});
