import { Form, Input, Button, message, Divider } from "antd";
import { useNavigate } from "react-router-dom";

export default function LoginPage() {
  const navigate = useNavigate();
  const [messageApi, contextHolder] = message.useMessage();

  const onFinish = async (values) => {
    try {
      const response = await fetch("/api/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        credentials: "include",
        body: JSON.stringify(values),
      });
      if (response.ok) {
        messageApi.success("Login successful");
        navigate("/restaurants");
      } else {
        messageApi.error("Invalid email or password");
      }
    } catch (error) {
      messageApi.error("Network error, please try again");
    }
  };

  return (
    <div style={{ maxWidth: 400, margin: "100px auto" }}>
      {contextHolder}
      <h2>Customer Login</h2>
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
        Are you a restaurant owner?{" "}
        <Button
          type="link"
          style={{ padding: 0 }}
          onClick={async () => {
            try {
              await fetch("/api/logout", { method: "POST", credentials: "include" });
            } catch (e) {}
            window.location.href = "/owner/login";
          }}
        >
          Owner Login →
        </Button>
      </div>
    </div>
  );
}
