import { StyleSheet, View, Text, Image, ScrollView } from 'react-native';
import { SafeAreaProvider, SafeAreaView } from 'react-native-safe-area-context';

import MainBg from './assets/images/mo-main-img.png';
import MotionIcon from "./assets/images/icon-motion.png";
import GroupIcon from "./assets/images/icon-group.png";
import ShareScreenIcon from "./assets/images/icon-sharescreen.png";
import MusicNoteIcon from "./assets/images/icon-musicnote.png";
import RankIcon from "./assets/images/icon-rank.png";

export default function App() {
  return (
    <SafeAreaProvider>
      <SafeAreaView style={styles.container}>
        <ScrollView>
          <View>
            <Text>STEPUP</Text>
          </View>
          <View>
            <Image source={MainBg} style={styles.mainBg}/>
          </View>
          <View style={styles.infoWrap}>
            <Text style={styles.infoTitle}>
              KPOP에 필요한{"\n"}
              모든 만남이{"\n"}
              있는 곳
            </Text>
            <View style={styles.infoMain}>
              <View style={styles.iconWrap}>
                <Image source={MotionIcon}/>
                <Text style={styles.iconText}>춤 모션인식</Text>
              </View>
              <View style={styles.iconWrap}>
                <Image source={GroupIcon}/>
                <Text style={styles.iconText}>대규모 랜플댄</Text>
              </View>
              <View style={styles.iconWrap}>
                <Image source={ShareScreenIcon}/>
                <Text style={styles.iconText}>화면공유</Text>
              </View>
              <View style={styles.iconWrap}>
                <Image source={MusicNoteIcon}/>
                <Text style={styles.iconText}>음원싱크</Text>
              </View>
              <View style={styles.iconWrap}>
                <Image source={RankIcon}/>
                <Text style={styles.iconText}>순위산출</Text>
              </View>
            </View>
          </View>
          <View style={styles.randomplay}>
            <Text style={styles.randomplayTitle}>언제, 어디서든 랜덤한 KPOP이 함께 나오는 곳</Text>
            <Text style={styles.randomplayInfo}>언제, 어디서든 장소에 구애받지 않고 원하는 춤을 출 수 있어요 당신의 시간에 맞게 랜플댄 무대가 제공됩니다.</Text>
            <Text style={styles.randomplayButton}>랜덤플레이댄스 참여하기</Text>
          </View>
          <View style={styles.realtime}>
            <View>
              <Text style={styles.realtimeTitle}>실시간 인기 랜플댄</Text>
              <Text style={styles.realtimeInfo}>현재 가장 인기가 많은 랜덤플레이댄스방에 참여해보세요.</Text>
            </View>
          </View>
        </ScrollView>
      </SafeAreaView>
    </SafeAreaProvider>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
  },
  mainBg:{
    height: 575,
    resizeMode: "cover",
  },
  infoWrap:{
    padding:40,
  },
  infoTitle: {
    fontSize: 22,
    fontWeight: 'bold',
    color: "#414149",
  },
  iconWrap:{
    display: "flex",
    alignItems: "center",
  },
  infoMain:{
    flexDirection: "row",
    justifyContent: "space-between",
    marginTop: 40,
  },
  iconText:{
    fontSize:9,
    color:"#858391",
    marginTop:6,
  },
  randomplay:{
    backgroundColor:"#EFF3FF",
    padding:60,
  },
  randomplayTitle:{
    fontSize: 22,
    fontWeight: "bold",
    color:"#3F4253",
  },
  randomplayInfo:{
    fontSize: 11,
    fontWeight: "regular",
    color: "#92969F",
    marginTop:5,
  },
  randomplayButton:{
    fontSize: 12,
    fontWeight: "light",
    color: "#506DF9",
    marginTop:9,
  },
  realtimeTitle:{

  },
  realtimeInfo:{
    
  }
});
