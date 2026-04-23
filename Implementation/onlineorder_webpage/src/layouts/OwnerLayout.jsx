import { useEffect, useState } from "react";
import { Layout, Menu, Button, Typography } from "antd";
import { UserOutlined } from "@ant-design/icons";
import { useNavigate, useLocation, Outlet } from "react-router-dom";

const { Header, Content } = Layout;
const { Text } = Typography;

export default function OwnerLayout() {
  const navigate = useNavigate();
  const location = useLocation();
  const [currentUser, setCurrentUser] = useState(null);

  useEffect(() => {
    fetch("/api/me", { credentials: "include" })
      .then((res) => (res.ok ? res.json() : null))
      .then((data) => setCurrentUser(data))
      .catch(() => setCurrentUser(null));
  }, [location.pathname]); // re-check on every page navigation

  const handleLogout = async () => {
    try {
      await fetch("/api/logout", { method: "POST", credentials: "include" });
    } catch (e) {
      // ignore network errors — redirect regardless
    }
    window.location.href = "/owner/login";
  };

  const navItems = [
    { key: "/owner/login",      label: "Login" },
    { key: "/owner/signup",     label: "Sign Up" },
    { key: "/owner/register",   label: "Register Restaurant" },
    { key: "/owner/dashboard",  label: "My Restaurants" },
  ];

  return (
    <Layout style={{ minHeight: "100vh" }}>
      <Header style={{ display: "flex", alignItems: "center", justifyContent: "space-between", paddingRight: 24 }}>
        {/* Left: navigation menu */}
        <Menu
          theme="dark"
          mode="horizontal"
          selectedKeys={[location.pathname]}
          items={navItems}
          onClick={({ key }) => navigate(key)}
          style={{ flex: 1, minWidth: 0 }}
        />

        {/* Right: login status */}
        <div style={{ display: "flex", alignItems: "center", gap: 12, whiteSpace: "nowrap" }}>
          {currentUser ? (
            <>
              <UserOutlined style={{ color: "#52c41a", fontSize: 16 }} />
              <Text style={{ color: "#52c41a", fontWeight: 500 }}>
                {currentUser.firstName} {currentUser.lastName}
                <span style={{ color: "#1677ff", marginLeft: 6, fontSize: 12 }}>(Owner)</span>
              </Text>
              <Button size="small" onClick={handleLogout}>
                Logout
              </Button>
            </>
          ) : (
            <Text style={{ color: "#faad14" }}>Not logged in</Text>
          )}
        </div>
      </Header>

      <Content style={{ padding: "24px" }}>
        <Outlet />
      </Content>
    </Layout>
  );
}
