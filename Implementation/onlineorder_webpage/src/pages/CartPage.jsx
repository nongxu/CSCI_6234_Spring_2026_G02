import { useEffect, useState } from "react";
import { Button, Card, message, Result, Spin } from "antd";
import { useNavigate } from "react-router-dom";

export default function CartPage() {
  const [total, setTotal] = useState(null);
  const [order, setOrder] = useState(null);
  const [loading, setLoading] = useState(true);
  const [messageApi, contextHolder] = message.useMessage();
  const navigate = useNavigate();

  useEffect(() => {
    fetch("/api/cart/summary", { credentials: "include" })
      .then((res) => {
        if (res.status === 401) {
          navigate("/login");
          return null;
        }
        if (res.status === 400) {
          setTotal(0);
          return null;
        }
        return res.json();
      })
      .then((data) => {
        if (data !== null) setTotal(data);
      })
      .catch(() => messageApi.error("Failed to load cart"))
      .finally(() => setLoading(false));
  }, []);

  const confirmOrder = async () => {
    try {
      const res = await fetch("/api/cart/confirm", {
        method: "POST",
        credentials: "include",
      });
      if (res.ok) {
        const data = await res.json();
        setOrder(data);
        messageApi.success("Order placed successfully");
      } else if (res.status === 400) {
        messageApi.warning("Your cart is empty");
      } else {
        messageApi.error("Failed to place order");
      }
    } catch {
      messageApi.error("Network error, please try again");
    }
  };

  if (loading)
    return <Spin style={{ margin: "100px auto", display: "block" }} />;

  if (order) {
    return (
      <div style={{ maxWidth: 400, margin: "100px auto" }}>
        {contextHolder}
        <Result
          status="success"
          title="Order Placed!"
          subTitle={`Order #${order.orderId} - Total: $${order.totalPrice}`}
          extra={
            <Button type="primary" onClick={() => navigate("/restaurants")}>
              Back to Restaurants
            </Button>
          }
        />
      </div>
    );
  }

  return (
    <div style={{ maxWidth: 400, margin: "100px auto" }}>
      {contextHolder}
      <h2>Your Cart</h2>
      <Card>
        {total === 0 ? (
          <p>Your cart is empty</p>
        ) : (
          <p>
            Total: <strong>${total}</strong>
          </p>
        )}
      </Card>
      <div style={{ marginTop: 16 }}>
        <Button
          onClick={() => navigate("/restaurants")}
          style={{ marginRight: 8 }}
        >
          Back to Restaurants
        </Button>
        {total > 0 && (
          <Button type="primary" onClick={confirmOrder}>
            Confirm Order
          </Button>
        )}
      </div>
    </div>
  );
}
