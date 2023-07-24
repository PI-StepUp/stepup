import "../styles/global.scss"
import "../styles/header.scss"
import "../styles/main.scss"
import "../styles/mainBanner.scss"
import "../styles/subnav.scss"
import "../styles/noticelist.scss"
import "../styles/articlelist.scss"
import "../styles/meetinglist.scss"
import "../styles/footer.scss"
import "../styles/login.scss"
import "../styles/signup.scss"
import "../styles/playlist.scss"
import "../styles/create.scss"
import "../styles/randomplaylist.scss"
import "../styles/sidemenu.scss"
import "../styles/practiceroom.scss"
import "../styles/language.scss"
import "../styles/mypage.css"
import "../styles/mypageedit.css"

import type {AppProps} from 'next/app';
import Head from "next/head";
import {RecoilRoot} from "recoil";

import LanguageButton from "components/LanguageButton"

const StepUp = ({Component} : AppProps) => {
    return (
        <>
            <Head>
                <meta name="viewport" content="width=device-width, initial-scale=1.0" />
            </Head>
            <RecoilRoot>
                <Component/>
                <LanguageButton/>
            </RecoilRoot>
        </>
    )
}

export default StepUp;