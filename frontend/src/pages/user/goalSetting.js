import React, { useState, useEffect } from 'react';
import Container from '../../components/utils/Container';
import Header from '../../components/utils/header';
import '../../assets/styles/goalSetting.css';
import toast, { Toaster } from 'react-hot-toast';
import UserService from '../../services/userService';
import AuthService from '../../services/auth.service';
import { useNavigate } from 'react-router-dom';
import confetti from 'canvas-confetti';

const fallingConfetti = () => {
  const duration = 5 * 1000;
  const end = Date.now() + duration;

  (function frame() {
    confetti({
      particleCount: 5,
      startVelocity: 30,
      ticks: 250,
      opacity: 0.9,
      spread: 180,
      origin: { x: Math.random(), y: 0 },
      shapes: ["circle", "square", "rectangle", "triangle"],
      colors: ["#e74c3c", "#f39c12", "#2980b9", "#2ecc71", "#8e44ad"],
      scalar: 1.5,
      drift: 0.2
    });
    if (Date.now() < end) {
      requestAnimationFrame(frame);
    }
  })();
};
const ErrorMessage = ({ message }) => message ? <span className="error-message" style={{ color: "red" }}>{message}</span> : null;

const GoalSetting = () => {
  const [goalName, setGoalName] = useState('');
  const [goalPrice, setGoalPrice] = useState('');
  const [goalDate, setGoalDate] = useState('');
  const [goalList, setGoalList] = useState([]);
  const [editingGoal, setEditingGoal] = useState(null);
  const [amountToAdd, setAmountToAdd] = useState({});
  const [errors, setErrors] = useState({});
  const navigate = useNavigate();

  const getGoalsByUserId = async () => {
    await UserService.getGoals(AuthService.getCurrentUser().email).then(
      (response) => {
        if (response.data.status === "SUCCESS") {
          setGoalList(response.data.response);
        }
      },
      (error) => {
        toast.error("Failed to fetch goals: Try again later!");
      }
    );
  };

  useEffect(() => {
    getGoalsByUserId();
  }, [goalList]);

  const validateForm = () => {
    const newErrors = {};
    if (!goalName.trim()) newErrors.goalName = 'Goal name is required';
    if (!goalPrice.trim() || isNaN(goalPrice) || Number(goalPrice) <= 0) newErrors.goalPrice = 'Enter a valid goal price';
    const targetDate = new Date(goalDate);
    if (!goalDate || isNaN(targetDate.getTime())) newErrors.goalDate = 'Enter a valid target date';
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleInputChange = (field, value) => {
    if (field === 'goalName') setGoalName(value);
    if (field === 'goalPrice') setGoalPrice(value);
    if (field === 'goalDate') setGoalDate(value);

    setErrors((prevErrors) => ({ ...prevErrors, [field]: '' }));
  };

  const handleAddGoal = async () => {
    if (!validateForm()) return;
    const targetDate = new Date(goalDate);

    const newGoal = {
      name: goalName,
      price: parseFloat(goalPrice),
      amountSaved: 0,
      targetDate: targetDate
    };

    await UserService.addGoal(AuthService.getCurrentUser().email, newGoal.name, newGoal.price, newGoal.targetDate).then(
      (response) => {
        if (response.data.status === "SUCCESS") {
          toast.success('Goal added successfully!');
          setGoalList([...goalList, newGoal]);
          setGoalName('');
          setGoalPrice('');
          setGoalDate('');
          navigate("/user/goalSetting", { state: { text: response.data.response } });
        }
      },
      (error) => {
        toast.error("Failed to create goals: Try again later!");
      }
    );
  };

  const handleEditGoal = (goal) => {
    setEditingGoal(goal);
    setGoalName(goal.name);
    setGoalPrice(goal.price.toString());
    const targetDate = goal.targetDate instanceof Date ? goal.targetDate : new Date(goal.targetDate);

    if (!isNaN(targetDate.getTime())) {
      setGoalDate(targetDate.toISOString().split('T')[0]);
    } else {
      toast.error('Invalid target date for this goal.', { position: 'top-center' });
    }
  };

  const handleSaveEditedGoal = async () => {
    const targetDate = new Date(goalDate);

    if (goalName.trim() === '' || goalPrice.trim() === '' || isNaN(goalPrice) || Number(goalPrice) <= 0 || !goalDate || isNaN(targetDate.getTime())) {
      toast.error('Please enter valid goal details.', { position: 'top-center' });
      return;
    }

    await UserService.updateGoal(AuthService.getCurrentUser().email, editingGoal.id, goalName, parseFloat(goalPrice), goalDate, editingGoal.amountSaved).then(
      (response) => {
        if (response.data.status === "SUCCESS") {
          toast.success('Goal updated successfully!', { position: 'top-center' });
          setGoalName('');
          setGoalPrice('');
          setGoalDate('');
          setEditingGoal(null);
          navigate("/user/goalSetting", { state: { text: response.data.response } });
        }
      },
      (error) => {
        toast.error("Failed to update goals: Try again later!");
      }
    );
  };

  const handleDeleteGoal = async (id) => {
    await UserService.deleteGoal(id).then(
      (response) => {
        if (response.data.status === "SUCCESS") {
          toast.success('Goal deleted successfully!', { position: 'top-center' });
          navigate("/user/goalSetting", { state: { text: response.data.response } });
        }
      },
      (error) => {
        toast.error("Failed to delete goals: Try again later!");
      }
    );
  };

  const handleAmountInputChange = (e, id) => {
    setAmountToAdd({ ...amountToAdd, [id]: e.target.value });
  };

  const handleAddAmountToGoal = async (goal) => {
    const amount = parseFloat(amountToAdd[goal.id]);
    const remainingAmount = calculateRemainingAmount(goal);

    if (isNaN(amount) || amount <= 0) {
      toast.error('Please enter a valid amount.', { position: 'top-center' });
      return;
    }

    if (amount > remainingAmount) {
      toast.error('You cannot add more than the remaining amount required to achieve the goal.', { position: 'top-center' });
      return;
    }

    const newAmountSaved = goal.amountSaved + amount;
    const updatedGoal = {
      ...goal,
      amountSaved: newAmountSaved >= goal.price ? goal.price : newAmountSaved
    };

    if (updatedGoal.amountSaved >= goal.price) {
      toast.success('Goal achieved! Congratulations!', { position: 'top-center' });
      fallingConfetti();
    }

    await UserService.updateGoal(AuthService.getCurrentUser().email, goal.id, goal.name, goal.price, goal.targetDate, updatedGoal.amountSaved).then(
      (response) => {
        if (response.data.status === "SUCCESS") {
          toast.success('Amount added successfully!', { position: 'top-center' });
          setAmountToAdd({ ...amountToAdd, [goal.id]: '' });
          navigate("/user/goalSetting", { state: { text: response.data.response } });
        }
      },
      (error) => {
        toast.error("Failed to add amount to goals: Try again later!");
      }
    );
  };

  const calculateRemainingAmount = (goal) => {
    const remainingAmount = goal.price - goal.amountSaved;
    return remainingAmount > 0 ? remainingAmount : 0;
  };

  const calculateMonthlySavings = (goal) => {
    const remainingAmount = calculateRemainingAmount(goal);
    const currentDate = new Date();
    const timeDiff = new Date(goal.targetDate) - currentDate;
    const remainingMonths = Math.ceil(timeDiff / (1000 * 3600 * 24 * 30));

    return remainingMonths > 0 ? (remainingAmount / remainingMonths).toFixed(2) : 0;
  };

  return (
    <Container activeNavId={12}>
      <Header title="Goal Setting" />
      <div className="goal-setting-container">
        <h1 className="page-title">Goal Setting</h1>
        <div className="goal-input">
          <input type="text" placeholder="Enter goal name" value={goalName} onChange={(e) => handleInputChange('goalName', e.target.value)} className="goal-input-field" />
          <ErrorMessage message={errors.goalName} />
          <input type="number" placeholder="Enter goal price" value={goalPrice} onChange={(e) => handleInputChange('goalPrice', e.target.value)} className="goal-input-field" />
          <ErrorMessage message={errors.goalPrice} />
          <input type="date" value={goalDate} onChange={(e) => handleInputChange('goalDate', e.target.value)} className="goal-input-field" min={new Date().toISOString().split("T")[0]} style={{ cursor: "pointer" }} />
          <ErrorMessage message={errors.goalDate} />
          <button
            onClick={editingGoal ? handleSaveEditedGoal : handleAddGoal}
            className="add-goal-btn"
            style={{ backgroundColor: "#4389df", borderRadius: "25px" }}
            onMouseEnter={(e) => {
              e.target.style.backgroundColor = 'green';
            }}
            onMouseLeave={(e) => {
              e.target.style.backgroundColor = '#007bff';
            }}
          >
            {editingGoal ? "Save Changes" : "Add Goal"}
          </button>

        </div>
        <h2 className="goal-list-title">Your Goals</h2>
        <ul className="goal-list">
          {goalList && goalList.length > 0 ? (
            goalList.map((goal) => (
              <li key={goal.id} className="goal-item">
                <div className="goal-details">
                  <h3>{goal.name}</h3>
                  <p><strong>Price:</strong> {goal.price.toFixed(2)}</p>
                  <p><strong>Amount Saved:</strong> {goal.amountSaved.toFixed(2)}</p>
                  <p><strong>Remaining Amount:</strong>{" "}{calculateRemainingAmount(goal).toFixed(2)}</p>
                  <p><strong>Monthly Savings Needed:</strong>{" "}{calculateMonthlySavings(goal)}</p>
                  <p><strong>Target Date:</strong>{" "}{new Date(goal.targetDate).toLocaleDateString()}</p>
                  <div className="progress-container">
                    <div className="progress-title">
                      <strong>Progress:</strong>
                    </div>
                    <div className="progress-bar" style={{ width: `${((goal.amountSaved / goal.price) * 100).toFixed(2)}%` }} />
                    <p>{`${((goal.amountSaved / goal.price) * 100).toFixed(2)}% Completed`}</p>
                  </div>
                </div>
                <div className="goal-actions">
                  {goal.amountSaved >= goal.price ? (
                    <>
                      <p style={{ color: "green", fontWeight: "bold", marginTop: "10px" }}>Congratulations! <br />You have achieved <br />this goal.</p>
                      <button onClick={() => handleDeleteGoal(goal.id)} className="delete-goal-btn" style={{ backgroundColor: "red", borderRadius: "25px" }}>Delete</button>
                    </>
                  ) : (
                    <>
                      <input type="number" placeholder="Enter Amount to Add" min="0" value={amountToAdd[goal.id] || ""} onChange={(e) => handleAmountInputChange(e, goal.id)} className="amount-input" />
                      <button onClick={() => handleAddAmountToGoal(goal)} className="add-amount-btn" style={{ backgroundColor: "blue", borderRadius: "25px" }} > Add Amount </button>
                      <button onClick={() => handleEditGoal(goal)} className="edit-goal-btn" style={{ backgroundColor: "orange", borderRadius: "25px" }}> Edit </button>
                      <button onClick={() => handleDeleteGoal(goal.id)} className="delete-goal-btn" style={{ backgroundColor: "red", borderRadius: "25px" }}> Delete </button>
                    </>
                  )}
                </div>
              </li>
            ))
          ) : (
            <p>No goals yet. Add one to get started!</p>
          )}
        </ul>
        <Toaster />
      </div>
    </Container>
  );
};

export default GoalSetting;
