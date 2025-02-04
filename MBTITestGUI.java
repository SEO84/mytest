package Shw1013_MBTI;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JWindow;
import javax.swing.Timer;

public class MBTITestGUI {
	// MBTI 점수를 저장하는 배열들
	private int[] ieScores = new int[2]; // [0]: I, [1]: E
	private int[] snScores = new int[2]; // [0]: S, [1]: N
	private int[] tfScores = new int[2]; // [0]: T, [1]: F
	private int[] jpScores = new int[2]; // [0]: J, [1]: P

	// 동물 및 식물에 대한 MBTI 매핑
	private static final char[][] MBTI_MAPPING = { { 'I', 'S', 'T', 'J' }, // 독수리, 소나무
			{ 'E', 'N', 'F', 'P' }, // 돌고래, 민들레
			{ 'I', 'S', 'F', 'P' }, // 팬더, 데이지
			{ 'E', 'N', 'T', 'J' }, // 늑대, 달리아
			{ 'I', 'N', 'F', 'J' }, // 고래, 라벤더
			{ 'E', 'S', 'T', 'P' }, // 말, 해바라기
			{ 'I', 'N', 'T', 'P' }, // 문어, 아이비
			{ 'E', 'S', 'F', 'J' }, // 개, 백합
			{ 'I', 'S', 'T', 'P' }, // 고양이, 선인장
			{ 'E', 'N', 'F', 'J' }, // 백조, 모란
			{ 'E', 'S', 'T', 'J' }, // 사자, 장미
			{ 'I', 'N', 'F', 'P' }, // 토끼, 자스민
			{ 'I', 'S', 'F', 'J' }, // 코끼리, 카밀레
			{ 'E', 'N', 'T', 'P' }, // 여우, 히아신스
			{ 'E', 'S', 'F', 'P' }, // 앵무새, 튤립
			{ 'I', 'N', 'T', 'J' } // 올빼미, 아마릴리스
	};

	private String userName; // 사용자의 이름
	private String userPhone; // 사용자의 전화번호
	private String userMBTI; // 사용자의 MBTI 유형
	private int currentStep = 0; // 현재 단계
	private boolean isAnimalStep = true; // 현재 단계가 동물 선택인지 여부
	private int animalSelectionCount = 0; // 동물 선택 횟수
	private int plantSelectionCount = 0; // 식물 선택 횟수
	private JFrame mainFrame; // 메인 프레임

	// 동물 및 식물 이름 배열
	private static final String[] ANIMALS = { "독수리", "돌고래", "팬더", "늑대", "고래", "말", "문어", "개", "고양이", "백조", "사자", "토끼",
			"코끼리", "여우", "앵무새", "올빼미" };
	private static final String[] PLANTS = { "소나무", "민들레", "데이지", "달리아", "라벤더", "해바라기", "아이비", "백합", "선인장", "모란", "장미",
			"자스민", "카밀레", "히아신스", "튤립", "아마릴리스" };

	private MBTITestDAO dao = new MBTITestDAO(); // 데이터베이스 접근 객체

	// 생성자: 사용자 이름과 전화번호를 받아 GUI 초기화
	public MBTITestGUI(String name, String phone) {
		this.userName = name;
		this.userPhone = phone;
		mainFrame = new JFrame("MBTI Test"); // 메인 프레임 생성
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 닫기 버튼 클릭 시 애플리케이션 종료
		mainFrame.setSize(800, 600); // 프레임 크기 설정

		// 화면 중앙에 프레임 위치시키기
		mainFrame.setLocationRelativeTo(null);

		updateUI(mainFrame); // UI 업데이트
		mainFrame.setVisible(true); // 프레임을 보이도록 설정
	}

	private void updateUI(JFrame frame) {
		// 기존의 모든 컴포넌트를 제거하여 UI를 초기화
		frame.getContentPane().removeAll();

		// 메인 패널 생성 및 설정 (BorderLayout 사용)
		JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // 패널에 여백 추가

		// 옵션을 표시할 패널 생성 (2x2 GridLayout 사용)
		JPanel panel = new JPanel(new GridLayout(2, 2, 20, 20));
		mainPanel.add(panel, BorderLayout.CENTER); // 메인 패널의 중앙에 옵션 패널 추가
		frame.add(mainPanel); // 프레임에 메인 패널 추가

		// 현재 단계에 따라 동물 또는 식물 옵션을 선택
		String[] currentOptions = isAnimalStep ? ANIMALS : PLANTS;
		int offset = (currentStep % 4) * 4; // 현재 단계에 따른 옵션의 시작 인덱스 계산

		// 4개의 옵션을 패널에 추가
		for (int i = 0; i < 4; i++) {
			if (offset + i < currentOptions.length) {
				String option = currentOptions[offset + i];
				ImageIcon originalIcon = loadImageIcon(option); // 옵션에 해당하는 이미지 로드

				if (originalIcon != null) {
					// 각 옵션을 위한 패널 생성
					JPanel optionPanel = new JPanel();
					optionPanel.setLayout(new BorderLayout(5, 5));

					// 이미지 크기 조절
					Image scaledImage = originalIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
					ImageIcon scaledIcon = new ImageIcon(scaledImage);

					// 버튼 생성 및 설정
					JButton button = new JButton(scaledIcon);
					button.setBorderPainted(false); // 버튼 테두리 제거
					button.setContentAreaFilled(false); // 버튼 배경 제거
					button.addActionListener(new ImageSelectionListener(option)); // 버튼 클릭 시 이벤트 리스너 추가

					// 라벨 생성 및 설정
					JLabel nameLabel = new JLabel(option, JLabel.CENTER);
					nameLabel.setFont(new Font("맑은 고딕", Font.BOLD, 14)); // 폰트 설정

					// 패널에 컴포넌트 추가
					optionPanel.add(button, BorderLayout.CENTER);
					optionPanel.add(nameLabel, BorderLayout.SOUTH);

					panel.add(optionPanel); // 옵션 패널을 메인 패널에 추가
				}
			}
		}

		// UI를 갱신하여 변경 사항 반영
		frame.revalidate();
		frame.repaint();
	}

	private ImageIcon loadImageIcon(String option) {
		// 이미지 경로를 생성
		String imagePath = "/images/" + option + ".jpg";
		// 이미지 URL을 가져옴
		java.net.URL imageUrl = getClass().getResource(imagePath);
		// 이미지가 존재하면 ImageIcon 생성, 그렇지 않으면 null 반환
		return imageUrl != null ? new ImageIcon(imageUrl) : null;
	}

	// 이미지 선택 시 동작을 정의하는 리스너 클래스
	private class ImageSelectionListener implements ActionListener {
		private final String option; // 선택된 옵션 이름

		public ImageSelectionListener(String option) {
			this.option = option; // 옵션 이름 초기화
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// 선택된 옵션에 따라 점수 업데이트
			updateScoreFromSelection(option);
			if (isAnimalStep) {
				animalSelectionCount++; // 동물 선택 횟수 증가
			} else {
				plantSelectionCount++; // 식물 선택 횟수 증가
			}
			currentStep++; // 현재 단계 증가
			if (currentStep % 4 == 0) {
				isAnimalStep = !isAnimalStep; // 4단계마다 동물/식물 단계 전환
			}
			if (animalSelectionCount + plantSelectionCount == 8) {
				showResult(); // 모든 선택이 완료되면 결과 표시
			} else {
				updateUI(mainFrame); // UI 업데이트
			}
		}
	}

	private void updateScoreFromSelection(String option) {
		// 현재 단계에 따라 선택된 옵션의 인덱스를 찾음
		int index = Arrays.asList(isAnimalStep ? ANIMALS : PLANTS).indexOf(option);
		if (index != -1) {
			// 해당 인덱스의 MBTI 문자를 가져와 점수 업데이트
			char[] mbtiChars = MBTI_MAPPING[index];
			updateScores(mbtiChars);
		}
	}

	private void updateScores(char[] mbtiChars) {
		// 각 MBTI 지표에 따라 점수 증가
		if (mbtiChars[0] == 'I')
			ieScores[0]++;
		else
			ieScores[1]++;
		if (mbtiChars[1] == 'S')
			snScores[0]++;
		else
			snScores[1]++;
		if (mbtiChars[2] == 'T')
			tfScores[0]++;
		else
			tfScores[1]++;
		if (mbtiChars[3] == 'J')
			jpScores[0]++;
		else
			jpScores[1]++;
	}

	private void showResult() {
		StringBuilder result = new StringBuilder();

		// MBTI 결과 계산
		result.append(ieScores[0] == ieScores[1] ? (Math.random() < 0.5 ? "I" : "E")
				: (ieScores[0] > ieScores[1] ? "I" : "E"));

		result.append(snScores[0] == snScores[1] ? (Math.random() < 0.5 ? "S" : "N")
				: (snScores[0] > snScores[1] ? "S" : "N"));

		result.append(tfScores[0] == tfScores[1] ? (Math.random() < 0.5 ? "T" : "F")
				: (tfScores[0] > tfScores[1] ? "T" : "F"));

		result.append(jpScores[0] == jpScores[1] ? (Math.random() < 0.5 ? "J" : "P")
				: (jpScores[0] > jpScores[1] ? "J" : "P"));

		userMBTI = result.toString(); // 최종 MBTI 결과 저장

		// DB에 결과 저장
		MBTITestDTO resultDTO = new MBTITestDTO(userName, userPhone, userMBTI);
		int success = dao.addResult(resultDTO);

		// MBTI 결과와 궁합 정보를 보여주는 다이얼로그 생성
		showMBTICompatibilityDialog(userMBTI);

		// 초기화면으로 돌아가기
		resetTest();
		showUserInputDialog();
		mainFrame.dispose(); // 현재 프레임 닫기
	}

	private static int calculateCompatibilityScore(String mbti1, String mbti2) {
		// 두 MBTI 유형이 동일한 경우
		if (mbti1.equals(mbti2)) {
			return 50; // 같은 유형끼리의 보통 궁합
		}

		// 두 MBTI 유형이 잘 맞는 경우
		if (isGoodMatch(mbti1, mbti2)) {
			return 80; // 잘 맞는 궁합의 경우
		}

		// 두 MBTI 유형이 도전적인 관계인 경우
		if (isChallengingMatch(mbti1, mbti2)) {
			return 20; // 도전적인 궁합의 경우
		}

		return 0; // 중립 궁합
	}

	// 두 MBTI 유형이 잘 맞는지 확인하는 메소드
	private static boolean isGoodMatch(String mbti1, String mbti2) {
		Map<String, List<String>> goodMatches = getMatches(true); // 잘 맞는 궁합 목록 가져오기
		return goodMatches.containsKey(mbti1) && goodMatches.get(mbti1).contains(mbti2);
	}

	// 두 MBTI 유형이 도전적인 관계인지 확인하는 메소드
	private static boolean isChallengingMatch(String mbti1, String mbti2) {
		Map<String, List<String>> challengingMatches = getMatches(false); // 도전적인 궁합 목록 가져오기
		return challengingMatches.containsKey(mbti1) && challengingMatches.get(mbti1).contains(mbti2);
	}

	// 잘 맞는 궁합 또는 도전적인 궁합 목록을 반환하는 메소드
	private static Map<String, List<String>> getMatches(boolean isGoodMatch) {
		Map<String, List<String>> matches = new HashMap<>();

		if (isGoodMatch) {
			// 잘 맞는 궁합 목록
			matches.put("ESTJ", Arrays.asList("ENFP", "ESFP", "ESTJ", "ESTP", "ISFP"));
			matches.put("ENTJ", Arrays.asList("ENFP", "ENTJ", "ENTP", "ESFP", "INFP"));
			matches.put("ESFJ", Arrays.asList("ENTP", "ESFJ", "ESFP", "ESTP", "ISTP"));
			matches.put("ENFJ", Arrays.asList("ENFJ", "ENFP", "ENTP", "ESTP", "INTP"));
			matches.put("ISTJ", Arrays.asList("ESFP", "INFP", "ISFP", "ISTJ", "ISTP"));
			matches.put("ISFJ", Arrays.asList("ESTP", "INTP", "ISFJ", "ISFP", "ISTP"));
			matches.put("INFJ", Arrays.asList("ENTP", "INFJ", "INFP", "INTP", "ISTP"));
			matches.put("INTJ", Arrays.asList("ENFP", "INFP", "INTJ", "INTP", "ISFP"));
			matches.put("ESTP", Arrays.asList("ENFJ", "ESFJ", "ESTJ", "ESTP", "ISFJ"));
			matches.put("ESFP", Arrays.asList("ENTJ", "ESFJ", "ESFP", "ESTJ", "ISTJ"));
			matches.put("ISTP", Arrays.asList("ESFJ", "INFJ", "ISFJ", "ISTJ", "ISTP"));
			matches.put("ISFP", Arrays.asList("ESTJ", "INTJ", "ISFJ", "ISFP", "ISTJ"));
			matches.put("INFP", Arrays.asList("ENTJ", "INFJ", "INFP", "INTJ", "ISTJ"));
			matches.put("INTP", Arrays.asList("ENFJ", "INFJ", "INTJ", "INTP", "ISFJ"));
			matches.put("ENFP", Arrays.asList("ENFJ", "ENFP", "ENTJ", "ESTJ", "INTJ"));
			matches.put("ENTP", Arrays.asList("ENFJ", "ENTJ", "ENTP", "ESFJ", "INFJ"));
		} else {
			// 도전적인 궁합 목록
			matches.put("ESTJ", Arrays.asList("ENFJ", "INFJ"));
			matches.put("ENTJ", Arrays.asList("ESFJ", "ISFJ"));
			matches.put("ESFJ", Arrays.asList("ENTJ", "INTJ"));
			matches.put("ENFJ", Arrays.asList("ESTJ", "ISTJ"));
			matches.put("ISTJ", Arrays.asList("ENFJ", "INFJ"));
			matches.put("ISFJ", Arrays.asList("ENTJ", "INTJ"));
			matches.put("INFJ", Arrays.asList("ESTJ", "ISTJ"));
			matches.put("INTJ", Arrays.asList("ESFJ", "ISFJ"));
			matches.put("ESTP", Arrays.asList("ENFP", "INFP"));
			matches.put("ESFP", Arrays.asList("ENTP", "INTP"));
			matches.put("ISTP", Arrays.asList("ENFP", "INFP"));
			matches.put("ISFP", Arrays.asList("ENTP", "INTP"));
			matches.put("INFP", Arrays.asList("ESTP", "ISTP"));
			matches.put("INTP", Arrays.asList("ESFP", "ISFP"));
			matches.put("ENFP", Arrays.asList("ESTP", "ISTP"));
			matches.put("ENTP", Arrays.asList("ENFP", "INFP"));
		}

		return matches;
	}

	private void showMBTICompatibilityDialog(String mbti) {
		// DB에서 모든 결과 가져오기
		List<MBTITestDTO> allResults = dao.getAllResults();

		// 궁합이 잘 맞는 사람들과 도전적인 관계의 사람들을 저장할 리스트
		List<String> goodMatches = new ArrayList<>();
		List<String> challengingMatches = new ArrayList<>();

		// 각 저장된 결과에 대해 궁합 확인
		for (MBTITestDTO person : allResults) {
			if (!person.getName().equals(userName)) { // 자기 자신 제외
				String compatibility = checkCompatibility(mbti, person.getMbti());
				if (compatibility.equals("GOOD")) {
					goodMatches.add(String.format("%s (%s)", person.getName(), person.getMbti()));
				} else if (compatibility.equals("BAD")) {
					challengingMatches.add(String.format("%s (%s)", person.getName(), person.getMbti()));
				}
			}
		}

		// 결과 메시지 생성
		StringBuilder message = new StringBuilder();
		message.append("당신의 MBTI는: ").append(mbti).append("\n\n");

		message.append("▶ 최고의 궁합인 사람들:\n");
		if (goodMatches.isEmpty()) {
			message.append("아직 데이터가 없습니다.\n");
		} else {
			for (String match : goodMatches) {
				message.append("- ").append(match).append("\n");
			}
		}

		message.append("\n▶ 최악의 궁합인 사람들:\n");
		if (challengingMatches.isEmpty()) {
			message.append("아직 데이터가 없습니다.\n");
		} else {
			for (String match : challengingMatches) {
				message.append("- ").append(match).append("\n");
			}
		}

		// 메시지 다이얼로그 표시
		JOptionPane.showMessageDialog(null, message.toString(), "MBTI 궁합 결과", JOptionPane.INFORMATION_MESSAGE);
	}

	private String checkCompatibility(String mbti1, String mbti2) {
		// 두 MBTI 유형이 잘 맞는지 확인
		if (isGoodMatch(mbti1, mbti2)) {
			return "GOOD";
		} else if (isChallengingMatch(mbti1, mbti2)) {
			return "BAD";
		}
		return "neutral"; // 중립적인 경우
	}

	// 테스트 초기화를 위한 메소드
	private void resetTest() {
		// 모든 점수 초기화
		ieScores = new int[2];
		snScores = new int[2];
		tfScores = new int[2];
		jpScores = new int[2];

		// 기타 변수들 초기화
		currentStep = 0;
		isAnimalStep = true;
		animalSelectionCount = 0;
		plantSelectionCount = 0;
		userName = null;
		userPhone = null;
		userMBTI = null;
	}

	// 사용자 입력을 받는 다이얼로그를 표시하는 메소드
	public static void showUserInputDialog() {
		JFrame inputFrame = new JFrame("Find Your MBTI!");
		inputFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		inputFrame.setSize(400, 300);
		inputFrame.setLayout(new GridBagLayout());
		inputFrame.setLocationRelativeTo(null);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(10, 10, 10, 10);

		JLabel nameLabel = new JLabel("이름:");
		gbc.gridx = 0;
		gbc.gridy = 0;
		inputFrame.add(nameLabel, gbc);

		JTextField nameField = new JTextField(15);
		gbc.gridx = 1;
		gbc.gridy = 0;
		inputFrame.add(nameField, gbc);

		JLabel phoneLabel = new JLabel("폰번호:");
		gbc.gridx = 0;
		gbc.gridy = 1;
		inputFrame.add(phoneLabel, gbc);

		JTextField phoneField = new JTextField(15);
		gbc.gridx = 1;
		gbc.gridy = 1;
		inputFrame.add(phoneField, gbc);

		JButton startButton = new JButton("시작");
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 2;
		inputFrame.add(startButton, gbc);

		JButton viewButton = new JButton("조회");
		gbc.gridy = 3;
		inputFrame.add(viewButton, gbc);

		JButton matchButton = new JButton("짝지 만들기");
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.gridwidth = 2;
		inputFrame.add(matchButton, gbc);

		matchButton.addActionListener(e -> showMatchingResults());

		startButton.addActionListener(e -> {
			String name = nameField.getText();
			String phone = phoneField.getText();
			if (!name.isEmpty() && !phone.isEmpty()) {
				inputFrame.dispose();
				new MBTITestGUI(name, phone);
			} else {
				JOptionPane.showMessageDialog(inputFrame, "모든 필드를 입력하세요.");
			}
		});

		viewButton.addActionListener(e -> showSavedRecords());

		inputFrame.setVisible(true);
	}

	// 저장된 기록을 보여주는 메소드
	private static void showSavedRecords() {
		JFrame recordsFrame = new JFrame("저장된 목록");
		recordsFrame.setSize(400, 300);
		recordsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		recordsFrame.setLocationRelativeTo(null);

		JTextArea recordsArea = new JTextArea();
		recordsArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(recordsArea);
		recordsFrame.add(scrollPane, BorderLayout.CENTER);

		// DB에서 데이터 조회
		MBTITestDAO dao = new MBTITestDAO();
		List<MBTITestDTO> results = dao.getAllResults();

		for (MBTITestDTO record : results) {
			recordsArea.append(String.format("ID: %d, 이름: %s, 폰번호: %s, MBTI: %s\n", record.getId(), record.getName(),
					record.getPhone(), record.getMbti()));
		}

		recordsFrame.setVisible(true);
	}

	// 매칭 결과를 보여주는 메소드
	private static void showMatchingResults() {
		JFrame matchFrame = new JFrame("짝지 만들기 결과");
		matchFrame.setSize(500, 400);
		matchFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		matchFrame.setLocationRelativeTo(null);

		JTextArea matchArea = new JTextArea();
		matchArea.setEditable(false);
		matchArea.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
		JScrollPane scrollPane = new JScrollPane(matchArea);
		matchFrame.add(scrollPane);

		// DB에서 데이터 가져오기
		MBTITestDAO dao = new MBTITestDAO();
		List<MBTITestDTO> allResults = dao.getAllResults();

		// 매칭 결과 계산 및 표시
		List<MatchPair> matches = findBestMatches(allResults);

		matchArea.append("🔥 MBTI 궁합 매칭 결과 🔥\n\n");
		for (MatchPair pair : matches) {
			if (pair.person2 != null) {
				matchArea.append(String.format("▶ 매칭 짝지\n- %s (%s)\n- %s (%s)\n궁합 점수: %d점\n\n", pair.person1.getName(),
						pair.person1.getMbti(), pair.person2.getName(), pair.person2.getMbti(),
						pair.compatibilityScore));
			} else {
				matchArea.append(
						String.format("▶ 매칭 대기\n- %s (%s)\n\n", pair.person1.getName(), pair.person1.getMbti()));
			}
		}

		matchFrame.setVisible(true);
	}

	// 매칭 쌍을 저장할 클래스
	private static class MatchPair {
		MBTITestDTO person1;
		MBTITestDTO person2;
		int compatibilityScore;

		MatchPair(MBTITestDTO person1, MBTITestDTO person2, int score) {
			this.person1 = person1;
			this.person2 = person2;
			this.compatibilityScore = score;
		}
	}

	// 가장 궁합이 좋은 쌍을 찾는 메소드
	private static List<MatchPair> findBestMatches(List<MBTITestDTO> people) {
		List<MatchPair> matches = new ArrayList<>();
		List<MBTITestDTO> unmatched = new ArrayList<>(people);

		while (unmatched.size() >= 2) {
			int bestScore = -1;
			MBTITestDTO bestPerson1 = null;
			MBTITestDTO bestPerson2 = null;

			// 가장 궁합이 좋은 쌍 찾기
			for (int i = 0; i < unmatched.size(); i++) {
				for (int j = i + 1; j < unmatched.size(); j++) {
					int score = calculateCompatibilityScore(unmatched.get(i).getMbti(), unmatched.get(j).getMbti());
					if (score > bestScore) {
						bestScore = score;
						bestPerson1 = unmatched.get(i);
						bestPerson2 = unmatched.get(j);
					}
				}
			}

			if (bestPerson1 != null && bestPerson2 != null) {
				matches.add(new MatchPair(bestPerson1, bestPerson2, bestScore));
				unmatched.remove(bestPerson1);
				unmatched.remove(bestPerson2);
			}
		}

		// 남은 한 명이 있다면 매칭 대기자로 추가
		if (!unmatched.isEmpty()) {
			matches.add(new MatchPair(unmatched.get(0), null, 0));
		}

		return matches;
	}

	// 스플래시 화면을 보여주는 메소드
	public static void showSplashScreen() {
		JWindow splash = new JWindow();
		ImageIcon splashIcon = new ImageIcon(MBTITestGUI.class.getResource("/images/splash.jpg"));

		if (splashIcon != null) {
			// 이미지 크기 조절 (원본의 2/3 크기)
			Image scaledImage = splashIcon.getImage().getScaledInstance(splashIcon.getIconWidth() / 3 * 2,
					splashIcon.getIconHeight() / 3 * 2, Image.SCALE_SMOOTH);

			JLabel splashLabel = new JLabel(new ImageIcon(scaledImage));
			splash.getContentPane().add(splashLabel);
			splash.pack();
			splash.setLocationRelativeTo(null); // 화면 중앙에 표시
			splash.setVisible(true);

			// 3초 후에 스플래시 화면을 닫고 메인 화면 표시
			Timer timer = new Timer(3000, e -> {
				splash.dispose();
				showUserInputDialog();
			});
			timer.setRepeats(false);
			timer.start();
		} else {
			// 스플래시 이미지를 찾을 수 없는 경우 바로 메인 화면 표시
			showUserInputDialog();
		}
	}
}
