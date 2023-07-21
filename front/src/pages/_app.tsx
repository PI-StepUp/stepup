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
import "../styles/noticecreate.scss"

import type {AppProps} from 'next/app';

const StepUp = ({Component} : AppProps) => {
    return (
        <>
            <Component/>
        </>
    )
}

export default StepUp;