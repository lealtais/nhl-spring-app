import { useState, useEffect, useCallback } from 'react';
import { NhlTeam, Game, Goal, NHL_TEAMS } from './types';
import { fetchTeamGames, fetchGameGoals } from './services/nhlService';
import TeamSelector from './components/TeamSelector';
import GameCarousel from './components/GameCarousel';
import ScoreHeader from './components/ScoreHeader';
import HockeyRink from './components/HockeyRink';
import GoalList from './components/GoalList';
import GoalDetailModal from './components/GoalDetailModal';

function getAccentColor(teamColor: string): string {
  // Parse hex color and check luminance
  const hex = teamColor.replace('#', '');
  const r = parseInt(hex.substring(0, 2), 16) / 255;
  const g = parseInt(hex.substring(2, 4), 16) / 255;
  const b = parseInt(hex.substring(4, 6), 16) / 255;

  const luminance = 0.2126 * r + 0.7152 * g + 0.0722 * b;

  if (luminance < 0.18) {
    return '#22D3EE'; // cyan-400
  }
  return teamColor;
}

export default function App() {
  const [selectedTeam, setSelectedTeam] = useState<NhlTeam | null>(NHL_TEAMS[0]);
  const [games, setGames] = useState<Game[]>([]);
  const [selectedGame, setSelectedGame] = useState<Game | null>(null);
  const [goals, setGoals] = useState<Goal[]>([]);
  const [selectedGoal, setSelectedGoal] = useState<Goal | null>(null);
  const [loadingGames, setLoadingGames] = useState(false);
  const [loadingGoals, setLoadingGoals] = useState(false);
  const [modalGoal, setModalGoal] = useState<Goal | null>(null);

  const teamColor = selectedTeam?.primaryColor ?? '#22D3EE';
  const accentColor = getAccentColor(teamColor);

  // Fetch games when team changes
  const handleTeamSelect = useCallback(async (team: NhlTeam) => {
    setSelectedTeam(team);
    setLoadingGames(true);
    setGames([]);
    setSelectedGame(null);
    setGoals([]);
    setSelectedGoal(null);

    const result = await fetchTeamGames(team.abbrev);
    setGames(result);
    setLoadingGames(false);

    if (result.length > 0) {
      handleGameSelect(result[0]);
    }
  }, []);

  // Fetch goals when game changes
  const handleGameSelect = useCallback(async (game: Game) => {
    setSelectedGame(game);
    setLoadingGoals(true);
    setGoals([]);
    setSelectedGoal(null);

    const result = await fetchGameGoals(game.id.toString());
    setGoals(result);
    setLoadingGoals(false);

    if (result.length > 0) {
      setSelectedGoal(result[0]);
    }
  }, []);

  const handleGoalSelect = useCallback((goal: Goal) => {
    setSelectedGoal(goal);
  }, []);

  const handleGoalDetail = useCallback((goal: Goal) => {
    setModalGoal(goal);
  }, []);

  // Auto-load first team on mount
  useEffect(() => {
    if (selectedTeam) {
      handleTeamSelect(selectedTeam);
    }
  }, []); // eslint-disable-line react-hooks/exhaustive-deps

  return (
    <div className="app">
      <header className="app-header">
        <div className="header-glow" style={{ background: `radial-gradient(ellipse at center, ${accentColor}15 0%, transparent 70%)` }} />
        <div className="header-content">
          <div className="header-logo">
            <svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke={accentColor} strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
              <circle cx="12" cy="12" r="10" />
              <path d="M12 2v20M2 12h20" />
            </svg>
            <h1>NHL Puck Tracker</h1>
          </div>

          <nav className="header-nav">
            <div className="header-season-badge">
              <span className="season-dot" />
              <span>2024-25 Season</span>
            </div>
            <a
              href="../LEAFS-NATION-SHOP-main/index.html"
              className="header-nav-link"
              style={{ color: accentColor }}
            >
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                <path d="M6 2L3 6v14a2 2 0 002 2h14a2 2 0 002-2V6l-3-4z" />
                <line x1="3" y1="6" x2="21" y2="6" />
                <path d="M16 10a4 4 0 01-8 0" />
              </svg>
              Leafs Shop
            </a>
          </nav>
        </div>
      </header>

      <main className="app-main">
        <TeamSelector
          teams={NHL_TEAMS}
          selectedTeam={selectedTeam}
          onTeamSelect={handleTeamSelect}
          accentColor={accentColor}
        />

        {loadingGames ? (
          <div className="loading-container">
            <div className="spinner" style={{ borderTopColor: accentColor }} />
            <p className="loading-text">Loading games...</p>
          </div>
        ) : games.length === 0 ? (
          <div className="empty-state">
            <p>No finished games found for this team.</p>
          </div>
        ) : (
          <GameCarousel
            games={games}
            selectedGame={selectedGame}
            selectedTeam={selectedTeam}
            onGameSelect={handleGameSelect}
            teamColor={teamColor}
            accentColor={accentColor}
          />
        )}

        {selectedGame && (
          <ScoreHeader game={selectedGame} accentColor={accentColor} />
        )}

        {loadingGoals ? (
          <div className="loading-container">
            <div className="spinner" style={{ borderTopColor: accentColor }} />
            <p className="loading-text">Loading goals...</p>
          </div>
        ) : goals.length === 0 && selectedGame ? (
          <div className="empty-state">
            <p>No goals recorded in this game or coordinate data unavailable.</p>
          </div>
        ) : goals.length > 0 ? (
          <>
            <HockeyRink
              goals={goals}
              selectedGoal={selectedGoal}
              onGoalSelect={handleGoalSelect}
              onGoalDetail={handleGoalDetail}
              accentColor={accentColor}
            />

            <GoalList
              goals={goals}
              selectedGoal={selectedGoal}
              onGoalSelect={handleGoalSelect}
              teamColor={teamColor}
              accentColor={accentColor}
            />
          </>
        ) : null}
      </main>

      {modalGoal && (
        <GoalDetailModal
          goal={modalGoal}
          accentColor={accentColor}
          onClose={() => setModalGoal(null)}
        />
      )}
    </div>
  );
}
