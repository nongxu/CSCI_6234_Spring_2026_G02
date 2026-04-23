import { useEffect, useState } from "react";
import { Card, Col, Row, Spin, Empty, Typography, Tag, message, Modal, List, Avatar } from "antd";
import { ShopOutlined } from "@ant-design/icons";
import { useNavigate } from "react-router-dom";

const { Title, Text } = Typography;

export default function OwnerDashboardPage() {
  const [restaurants, setRestaurants] = useState([]);
  const [loading, setLoading] = useState(true);
  const [selectedRestaurant, setSelectedRestaurant] = useState(null);
  const [menuItems, setMenuItems] = useState([]);
  const [menuLoading, setMenuLoading] = useState(false);
  const [messageApi, contextHolder] = message.useMessage();
  const navigate = useNavigate();

  const handleCardClick = (restaurant) => {
    setSelectedRestaurant(restaurant);
    setMenuItems([]);
    setMenuLoading(true);
    fetch(`/api/restaurants/${restaurant.restaurantId}/menu`, { credentials: "include" })
      .then((res) => res.json())
      .then((data) => setMenuItems(data))
      .catch(() => messageApi.error("Failed to load menu"))
      .finally(() => setMenuLoading(false));
  };

  useEffect(() => {
    fetch("/api/owner/restaurants", { credentials: "include" })
      .then((res) => {
        if (res.status === 401) {
          navigate("/owner/login");
          return null;
        }
        return res.json();
      })
      .then((data) => {
        if (data !== null) setRestaurants(data);
      })
      .catch(() => messageApi.error("Failed to load restaurants"))
      .finally(() => setLoading(false));
  }, []);

  if (loading)
    return <Spin style={{ margin: "100px auto", display: "block" }} />;

  return (
    <div style={{ maxWidth: 960, margin: "0 auto" }}>
      {contextHolder}
      <Title level={3}>
        <ShopOutlined style={{ marginRight: 8 }} />
        My Restaurants
      </Title>

      {restaurants.length === 0 ? (
        <Empty
          description="You have not registered any restaurants yet"
          style={{ marginTop: 80 }}
        />
      ) : (
        <Row gutter={[24, 24]}>
          {restaurants.map((r) => (
            <Col xs={24} sm={12} md={8} key={r.restaurantId}>
              <Card
                hoverable
                onClick={() => handleCardClick(r)}
                cover={
                  r.image ? (
                    <img
                      alt={r.name}
                      src={`/api${r.image}`}
                      style={{ height: 180, objectFit: "cover" }}
                    />
                  ) : (
                    <div
                      style={{
                        height: 180,
                        background: "#f0f0f0",
                        display: "flex",
                        alignItems: "center",
                        justifyContent: "center",
                        color: "#bbb",
                        fontSize: 14,
                      }}
                    >
                      No Image
                    </div>
                  )
                }
              >
                <Card.Meta
                  title={r.name}
                  description={
                    <div>
                      <div>
                        <Text type="secondary">📍 {r.address}</Text>
                      </div>
                      <div style={{ marginTop: 4 }}>
                        <Text type="secondary">📞 {r.phone}</Text>
                      </div>
                      <div style={{ marginTop: 8 }}>
                        <Tag color="blue">ID: {r.restaurantId}</Tag>
                      </div>
                    </div>
                  }
                />
              </Card>
            </Col>
          ))}
        </Row>
      )}

      <Modal
        title={selectedRestaurant?.name + " — Menu"}
        open={!!selectedRestaurant}
        onCancel={() => setSelectedRestaurant(null)}
        footer={null}
        width={560}
      >
        <Spin spinning={menuLoading}>
          {menuItems.length === 0 && !menuLoading ? (
            <Empty description="No menu items registered" />
          ) : (
            <List
              dataSource={menuItems}
              renderItem={(item) => (
                <List.Item>
                  <List.Item.Meta
                    avatar={
                      item.image ? (
                        <Avatar
                          shape="square"
                          size={56}
                          src={`/api${item.image}`}
                        />
                      ) : (
                        <Avatar shape="square" size={56}>
                          {item.name[0]}
                        </Avatar>
                      )
                    }
                    title={item.name}
                    description={item.description || <Text type="secondary">No description</Text>}
                  />
                  <Text strong>${item.price.toFixed(2)}</Text>
                </List.Item>
              )}
            />
          )}
        </Spin>
      </Modal>
    </div>
  );
}
