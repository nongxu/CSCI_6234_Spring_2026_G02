import { useEffect, useState } from "react";
import {
  Button,
  Collapse,
  Empty,
  message,
  Spin,
  Table,
  Tag,
  Typography,
} from "antd";
import { OrderedListOutlined } from "@ant-design/icons";
import { useNavigate } from "react-router-dom";

const { Title, Text } = Typography;

const STATUS_COLOR = {
  PENDING: "gold",
  CANCELLED: "red",
};

export default function OrderHistoryPage() {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [cancellingId, setCancellingId] = useState(null);
  const [messageApi, contextHolder] = message.useMessage();
  const navigate = useNavigate();

  useEffect(() => {
    fetchOrders();
  }, []);

  const fetchOrders = () => {
    setLoading(true);
    fetch("/api/orders", { credentials: "include" })
      .then((res) => {
        if (res.status === 401) {
          navigate("/login");
          return null;
        }
        return res.json();
      })
      .then((data) => {
        if (data !== null) {
          // Show most recent orders first
          setOrders(data.slice().reverse());
        }
      })
      .catch(() => messageApi.error("Failed to load orders"))
      .finally(() => setLoading(false));
  };

  const handleCancel = async (orderId) => {
    setCancellingId(orderId);
    try {
      const res = await fetch(`/api/orders/${orderId}/cancel`, {
        method: "PUT",
        credentials: "include",
      });
      if (res.ok) {
        messageApi.success(`Order #${orderId} has been cancelled`);
        fetchOrders();
      } else {
        const text = await res.text();
        messageApi.error(text || "Failed to cancel order");
      }
    } catch {
      messageApi.error("Network error, please try again");
    } finally {
      setCancellingId(null);
    }
  };

  const itemColumns = [
    { title: "Item", dataIndex: "menuItemName", key: "menuItemName" },
    { title: "Qty", dataIndex: "quantity", key: "quantity", width: 60 },
    {
      title: "Price",
      dataIndex: "price",
      key: "price",
      width: 80,
      render: (p) => `$${p.toFixed(2)}`,
    },
    {
      title: "Subtotal",
      key: "subtotal",
      width: 90,
      render: (_, r) => `$${(r.price * r.quantity).toFixed(2)}`,
    },
  ];

  if (loading)
    return <Spin style={{ margin: "100px auto", display: "block" }} />;

  const collapseItems = orders.map((order) => ({
    key: String(order.orderId),
    label: (
      <div
        style={{
          display: "flex",
          alignItems: "center",
          gap: 16,
          flexWrap: "wrap",
        }}
      >
        <Text strong>Order #{order.orderId}</Text>
        <Tag color={STATUS_COLOR[order.status] ?? "default"}>
          {order.status}
        </Tag>
        <Text type="secondary" style={{ fontSize: 12 }}>
          {new Date(order.createdAt).toLocaleString()}
        </Text>
        <Text strong style={{ marginLeft: "auto" }}>
          Total: ${order.totalPrice.toFixed(2)}
        </Text>
      </div>
    ),
    children: (
      <div>
        <Table
          dataSource={order.items}
          columns={itemColumns}
          rowKey="menuItemName"
          pagination={false}
          size="small"
          footer={() => <strong>Total: ${order.totalPrice.toFixed(2)}</strong>}
        />
        {order.status === "PENDING" && (
          <div style={{ marginTop: 12 }}>
            <Button
              danger
              size="small"
              loading={cancellingId === order.orderId}
              onClick={() => handleCancel(order.orderId)}
            >
              Cancel Order
            </Button>
          </div>
        )}
      </div>
    ),
  }));

  return (
    <div style={{ maxWidth: 760, margin: "0 auto" }}>
      {contextHolder}
      <Title level={3}>
        <OrderedListOutlined style={{ marginRight: 8 }} />
        My Orders
      </Title>

      {orders.length === 0 ? (
        <Empty
          description="You have not placed any orders yet"
          style={{ marginTop: 80 }}
        >
          <Button type="primary" onClick={() => navigate("/restaurants")}>
            Browse Restaurants
          </Button>
        </Empty>
      ) : (
        <Collapse
          accordion={false}
          items={collapseItems}
          style={{ background: "#fff" }}
        />
      )}
    </div>
  );
}
