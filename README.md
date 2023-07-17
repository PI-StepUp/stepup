| 커밋 유형 | 아이콘 | 코드 | 의미 |
| --- | --- | --- | --- |
| Feat | ➕ | `:heavy_plus_sign:` | 새로운 기능 추가 |
| Fix | 🐛 | `:bug:` | 버그 수정 |
| Docs | 📝 | `:memo:` | 문서 수정 ex) .gitignore, swagger, README |
| Style | ✨ | `:sparkles:` | 코드 formatting, 세미콜론 누락, 코드 자체의 변경이 없는 경우 |
| Refactor | 🔨 | `:hammer:` | 코드 리팩토링 (기능은 그대로, 코드만 변경된 경우) |
| Test | ✅ | `:white_check_mark:` | 테스트 코드, 리팩토링 테스트 코드 추가 |
| Design | 🎨 | `:art:` | CSS 등 사용자 UI 디자인 변경 |
| Comment | 💬 | `:speech_balloon:` | 필요한 주석 추가 및 변경 |
| Rewrite | 📦 | `:package:` | 파일 또는 폴더 명 수정/이동/삭제 |
| !CHANGE | ⚡️ | `:zap:` | 기능에 변경 사항이 생긴 경우 |
| !HOTFIX | 🔥 | `:fire:` | 급하게 치명적인 버그를 고쳐야 하는 경우 |
| Merge | 🔀 | `:twisted_rightwards_arrows:` | 브랜치 합병하는 경우 |
| Rewind | ⏪ | `:rewind:` | 이전 커밋으로 돌아가는 경우 |
| Infra | 🌐 | `:globe_with_meridians:` | 배포 |

ex) ==========================

➕Feat(user): 회원가입 api 개발 (#~~)

- 회원가입 api controller 추가
- 회원가입 api service 추가
- 회원가입 api repository 추가

==============================

<aside>
💡 **GIT Flow**

1. master에서 develop으로 가져와
2. develop에서 Ver.N 별로 구현할 기능을 feat/function으로 가져와
3. feat/function에서 구현
4. 구현이 끝나면 develop에 merge
5. Ver.N 구현이 끝나면 release에 merge
6. release에서 추가 테스트, 동작 확인 진행 후 master에 merge
    1. 오류가 발생했다면 release에서 바로 수정
7. 1~6 반복

 * master에서 에러 발생 시 hotfix로 가져와서 수정 후 다시 master에 merge

</aside>
