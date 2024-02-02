import { Outlet } from "react-router-dom";
import SideBar from "../../components/SideBar/index";
import Footer from "../../components/Footer/Footer";
import "./index.css"
const MainLayout = () => {
  return (
    <>
 <div className="main-layout">
      <SideBar />
     
    </div>


    {/* <Footer /> */}
    </>
  );
};
export default MainLayout;
