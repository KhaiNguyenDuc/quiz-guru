import { useRoutes } from "react-router-dom";
import MainLayout from "../layout/MainLayout";

import Error from "../components/Error.js";
import HomePage from "../pages/HomePage/index.js";
import QuizPage from "../pages/QuizPage/index.js";
import LoginPage from "../pages/LoginPage/index.js";
import AuthLayout from "../layout/AuthLayout/index.js";
import RegisterPage from "../pages/RegisterPage/index.js";
import RejectRoute from "./RejectRoute.js";
import QuizListPage from "../pages/QuizListPage/index.js";
import RecordPage from "../pages/RecordPage/index.js";
import RecordDetailPage from "../pages/RecordDetailPage/index.js";

import VocabularyPage from "../pages/VocabularyPage/index.js";
import LibraryPage from "../pages/LibraryPage/index.js";
import WordPage from "../pages/WordPage/index.js";
import ErrorPage from "../pages/ErrorPage/ErrorPage.js";
import ProtectedRoute from "./ProtectedRoute.js";
import FindAccountPage from "../pages/FindAccountPage/index.js";
import ResetPasswordPage from "../pages/ResetPasswordPage/index.js";
import VerifyPage from "../pages/VerifyPage/index.js";
function useRouteElements() {



  const routeElements = useRoutes([
    {
      path: '/auth',
      element: <RejectRoute><AuthLayout/></RejectRoute>,
      children: [
        {
          path: '/auth/login',
          element: <LoginPage/>
        },
        {
          path: '/auth/register',
          element: <RegisterPage/>
        },
        {
          path: '/auth/verify',
          element: <VerifyPage/>
        },
        {
          path: '/auth/find-account',
          element: <FindAccountPage/>
        },
        {
          path: '/auth/reset-password',
          element: <ResetPasswordPage/>
        }
      ]
    },
    {
        path: '/',
        element: <ProtectedRoute><MainLayout /></ProtectedRoute>,
        children: [
          {
            path: '',
            element: <HomePage />
          },
          {
            path: '/normal/create/:option',
            element: <HomePage />
          },
          {
            path: '/quiz/:id',
            element: <QuizPage/>
          },
          {
            path: "internal-error",
            element: <ErrorPage status="500" message={"Có lỗi xảy ra"}
            title={"Lỗi hệ thống"} />
          },
          {
            path: '*',
            element: <ErrorPage status="404" message={"Không tìm thấy trang bạn yêu cầu"}
            title={"Không tìm thấy trang"} />
          },
  
        ]
      },    
      {
        path: '/member',
        element:  <ProtectedRoute><MainLayout /></ProtectedRoute>,
        children: [
          {
            path: '/member/quiz',
            element: <QuizListPage/>
          },
          {
            path: '/member/library',
            element: <LibraryPage/>
          },
          {
            path: '/member/word-set/:wordSetId',
            element: <WordPage />
          },
          {
            path: '/member/vocabulary/create',
            element: <VocabularyPage/>
          },
          {
            path: '/member/vocabulary/create/:option',
            element: <VocabularyPage/>
          },
          {
            path: '/member/record',
            element: <RecordPage/>
          },
          {
            path: '/member/record/:id',
            element: <RecordDetailPage/>
          },
        ]
      },
  ])
  return routeElements
}

export default useRouteElements;
