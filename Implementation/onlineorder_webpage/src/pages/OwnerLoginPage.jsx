import { Form, Input, Button, message, Divider } from "antd";
import { useNavigate } from "react-router-dom";

export default function OwnerLoginPage() {
  const navigate = useNavigate();
  const [messageApi, contextHolder] = message.useMessage();

  const onFinish = async (values) => {
    try {
      const res = await fetch("/api/owner/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        credentials: "include",
        body: JSON.stringify(values),
      });
      if (res.ok) {
        messageApi.success("Login successful");
        setTimeout(() => navigate("/owner/register"), 1000);
      } else {
        messageApi.error("Invalid email or password");
      }
    } catch {
      messageApi.error("Network error, please try again");
    }
  };

  return (
    <div style={{ maxWidth: 400, margin: "100px auto" }}>
      {contextHolder}
      <h2>Restaurant Owner Login</h2>
      <Form layout="vertical" onFinish={onFinish}>
        <Form.Item
          label="Email"
          name="email"
          rules={[{ required: true, message: "Please enter your email" }]}
        >
          <Input />
        </Form.Item>
        <Form.Item
          label="Password"
          name="password"
          rules={[{ required: true, message: "Please enter your password" }]}
        >
          <Input.Password />
        </Form.Item>
        <Form.Item>
          <Button type="primary" htmlType="submit" block>
            Login
          </Button>
        </Form.Item>
      </Form>

      <Divider />
      <div style={{ textAlign: "center", color: "#888" }}>
        Are you a customer?{" "}
        <Button
          type="link"
          style={{ padding: 0 }}
          onClick={async () => {
            try {
              await fetch("/api/logout", { method: "POST", credentials: "include" });
            } catch (e) {}
            window.location.href = "/login";
          }}
        >
          Customer Login →
        </Button>
      </div>
    </div>
  );
}
