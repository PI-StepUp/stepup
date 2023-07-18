import "../styles/global.scss"
import "../styles/header.scss"
import "../styles/main.scss"
import "../styles/mainBanner.scss"
import "../styles/subnav.scss"
import "../styles/noticelist.scss"
import "../styles/articlelist.scss"

import type {AppProps} from 'next/app';

const StepUp = ({Component} : AppProps) => {
    return (
        <>
            <Component/>
        </>
    )
}

export default StepUp;